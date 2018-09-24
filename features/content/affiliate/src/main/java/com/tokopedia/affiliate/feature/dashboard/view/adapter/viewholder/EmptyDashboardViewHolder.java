package com.tokopedia.affiliate.feature.dashboard.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.Button;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.feature.dashboard.view.listener.DashboardContract;
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.EmptyDashboardViewModel;

/**
 * @author by yfsx on 19/09/18.
 */
public class EmptyDashboardViewHolder extends AbstractViewHolder<EmptyDashboardViewModel> {

    private DashboardContract.View mainView;

    @LayoutRes
    public static final int LAYOUT = R.layout.item_af_dashboard_empty;

    private Button btnStartRecommendation;

    public EmptyDashboardViewHolder(View itemView, DashboardContract.View mainView) {
        super(itemView);
        this.mainView = mainView;
        btnStartRecommendation = (Button) itemView.findViewById(R.id.btn_start_recommendation);
    }

    @Override
    public void bind(EmptyDashboardViewModel element) {
        initViewListener();
    }

    private void initViewListener() {
        btnStartRecommendation.setOnClickListener(view -> {

        });
    }
}
