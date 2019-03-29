package com.tokopedia.flight.cancellation.view.fragment.customview;

import android.app.Dialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;

import com.tokopedia.design.component.BottomSheets;
import com.tokopedia.flight.R;
import com.tokopedia.flight.cancellation.view.adapter.FlightCancellationRefundAdapter;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationRefund;

import java.util.ArrayList;
import java.util.List;

public class FlightCancellationRefundBottomSheet extends BottomSheets {
    private RecyclerView detailsRecyclerView;

    @Override
    public int getLayoutResourceId() {
        return R.layout.partial_flight_cancellation_refund_description;
    }

    @Override
    public void initView(View view) {
        detailsRecyclerView = view.findViewById(R.id.rv_details);
        String[] titles = getResources().getStringArray(R.array.flight_cancellation_refund_title);
        String[] subtitles = getResources().getStringArray(R.array.flight_cancellation_refund_subtitle);
        List<FlightCancellationRefund> descriptions = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            FlightCancellationRefund description = new FlightCancellationRefund();
            description.setTitle(titles[i]);
            description.setSubtitle(subtitles[i]);
            descriptions.add(description);
        }
        FlightCancellationRefundAdapter adapter = new FlightCancellationRefundAdapter();
        adapter.setDescriptions(descriptions);
        detailsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        detailsRecyclerView.setAdapter(adapter);
    }

    @Override
    protected String title() {
        return getString(R.string.flight_order_status_refund_label);
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        if (metrics.heightPixels > 0) {
            if (getBottomSheetBehavior() != null)
                getBottomSheetBehavior().setPeekHeight(metrics.heightPixels / 2);
        }
    }
}
