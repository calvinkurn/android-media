package com.tokopedia.sellerapp.dashboard.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.design.ticker.TickerView;
import com.tokopedia.seller.common.widget.LabelView;
import com.tokopedia.sellerapp.R;

/**
 * Created by nathan on 9/6/17.
 */

public class DashboardFragment extends BaseDaggerFragment {

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    private TickerView tickerView;

    private LabelView newOrderLabelView;
    private LabelView deliveryConfirmationLabelView;
    private LabelView deliveryStatusLabelView;
    private LabelView opportunityLabelView;
    private LabelView messageLabelView;
    private LabelView discussionLabelView;
    private LabelView reviewLabelView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tickerView = (TickerView) view.findViewById(R.id.ticker_view);
        newOrderLabelView = (LabelView) view.findViewById(R.id.label_view_new_order);
        deliveryConfirmationLabelView = (LabelView) view.findViewById(R.id.label_view_delivery_confirmation);
        deliveryStatusLabelView = (LabelView) view.findViewById(R.id.label_view_delivery_status);
        opportunityLabelView = (LabelView) view.findViewById(R.id.label_view_opportunity);
        messageLabelView = (LabelView) view.findViewById(R.id.label_view_message);
        discussionLabelView = (LabelView) view.findViewById(R.id.label_view_discussion);
        reviewLabelView = (LabelView) view.findViewById(R.id.label_view_review);
        newOrderLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        deliveryConfirmationLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        deliveryStatusLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        opportunityLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        messageLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        discussionLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        reviewLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }
}
