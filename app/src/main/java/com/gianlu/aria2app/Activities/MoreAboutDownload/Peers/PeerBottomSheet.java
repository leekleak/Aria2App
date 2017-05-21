package com.gianlu.aria2app.Activities.MoreAboutDownload.Peers;

import android.support.annotation.NonNull;
import android.view.View;

import com.gianlu.aria2app.NetIO.JTA2.Peer;
import com.gianlu.aria2app.R;
import com.gianlu.aria2app.Utils;
import com.gianlu.commonutils.BaseBottomSheet;
import com.gianlu.commonutils.CommonUtils;
import com.gianlu.commonutils.SuperTextView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;

import java.util.List;

public class PeerBottomSheet extends BaseBottomSheet<Peer> {
    private final SuperTextView downloadSpeed;
    private final SuperTextView uploadSpeed;
    private final LineChart chart;
    private final SuperTextView seeder;
    private final SuperTextView peerChoking;
    private final SuperTextView amChoking;

    public PeerBottomSheet(View sheet) {
        super(sheet, R.layout.peer_sheet);

        this.downloadSpeed = (SuperTextView) content.findViewById(R.id.peerSheet_downloadSpeed);
        this.uploadSpeed = (SuperTextView) content.findViewById(R.id.peerSheet_uploadSpeed);
        this.chart = (LineChart) content.findViewById(R.id.peerSheet_chart);
        this.seeder = (SuperTextView) content.findViewById(R.id.peerSheet_seeder);
        this.peerChoking = (SuperTextView) content.findViewById(R.id.peerSheet_peerChoking);
        this.amChoking = (SuperTextView) content.findViewById(R.id.peerSheet_amChoking);
    }

    @Override
    protected int getRippleDark() {
        return R.drawable.ripple_effect_dark;
    }

    @Override
    protected void setupView(@NonNull Peer peer) {
        title.setText(peer.ip + ":" + peer.port);
        Utils.setupChart(chart, true);
    }

    @Override
    protected void updateView(@NonNull Peer peer) {
        downloadSpeed.setText(CommonUtils.speedFormatter(peer.downloadSpeed, false));
        uploadSpeed.setText(CommonUtils.speedFormatter(peer.uploadSpeed, false));

        LineData data = chart.getLineData();
        if (data != null) {
            int pos = data.getEntryCount() + 1;
            data.addEntry(new Entry(pos, peer.downloadSpeed), Utils.CHART_DOWNLOAD_SET);
            data.addEntry(new Entry(pos, peer.uploadSpeed), Utils.CHART_UPLOAD_SET);
            data.notifyDataChanged();
            chart.notifyDataSetChanged();
            chart.setVisibleXRangeMaximum(60);
            chart.moveViewToX(data.getEntryCount());
        }

        seeder.setHtml(R.string.seeder, String.valueOf(peer.seeder));
        peerChoking.setHtml(R.string.peerChoking, String.valueOf(peer.peerChoking));
        amChoking.setHtml(R.string.amChoking, String.valueOf(peer.amChoking));
    }

    public void update(List<Peer> peers) {
        if (current == null) return;
        update(Peer.find(peers, current.peerId));
    }
}