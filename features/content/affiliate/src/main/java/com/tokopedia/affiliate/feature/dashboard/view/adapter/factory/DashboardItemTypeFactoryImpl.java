package com.tokopedia.affiliate.feature.dashboard.view.adapter.factory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.affiliate.feature.dashboard.view.adapter.viewholder.DashboardItemViewHolder;
import com.tokopedia.affiliate.feature.dashboard.view.listener.DashboardListener;
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.DashboardItemViewModel;
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.EmptyDashboardViewModel;

/**
 * @author by yfsx on 18/09/18.
 */
public class DashboardItemTypeFactoryImpl extends BaseAdapterTypeFactory implements DashboardItemTypeFactory {

    private DashboardListener.View mainView;

    public DashboardItemTypeFactoryImpl(DashboardListener.View mainView) {
        this.mainView = mainView;
    }

    @Override
    public int type(DashboardItemViewModel dashboardItemViewModel) {
        return DashboardItemViewHolder.LAYOUT;
    }

    @Override
    public int type(EmptyDashboardViewModel emptyDashboardViewModel) {
        return 0;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int viewType) {
        AbstractViewHolder abstractViewHolder;
        if (viewType == DashboardItemViewHolder.LAYOUT) {
            abstractViewHolder = new DashboardItemViewHolder(view, mainView);
        } else {
            abstractViewHolder = super.createViewHolder(view, viewType);
        }
        return abstractViewHolder;
    }
}
