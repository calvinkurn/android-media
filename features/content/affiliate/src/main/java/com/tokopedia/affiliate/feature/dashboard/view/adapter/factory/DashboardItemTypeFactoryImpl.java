package com.tokopedia.affiliate.feature.dashboard.view.adapter.factory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.affiliate.feature.dashboard.view.adapter.viewholder.DashboardHeaderViewHolder;
import com.tokopedia.affiliate.feature.dashboard.view.adapter.viewholder.DashboardItemViewHolder;
import com.tokopedia.affiliate.feature.dashboard.view.adapter.viewholder.EmptyDashboardViewHolder;
import com.tokopedia.affiliate.feature.dashboard.view.listener.DashboardContract;
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.DashboardHeaderViewModel;
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.DashboardItemViewModel;
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.EmptyDashboardViewModel;

/**
 * @author by yfsx on 18/09/18.
 */
public class DashboardItemTypeFactoryImpl extends BaseAdapterTypeFactory implements DashboardItemTypeFactory {

    private DashboardContract.View mainView;

    public DashboardItemTypeFactoryImpl(DashboardContract.View mainView) {
        this.mainView = mainView;
    }

    @Override
    public int type(DashboardItemViewModel dashboardItemViewModel) {
        return DashboardItemViewHolder.LAYOUT;
    }

    @Override
    public int type(DashboardHeaderViewModel dashboardInfoViewModel) {
        return DashboardHeaderViewHolder.LAYOUT;
    }


    @Override
    public int type(EmptyDashboardViewModel emptyDashboardViewModel) {
        return EmptyDashboardViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int viewType) {
        AbstractViewHolder abstractViewHolder;
        if (viewType == DashboardItemViewHolder.LAYOUT) {
            abstractViewHolder = new DashboardItemViewHolder(view, mainView);
        } else if(viewType == DashboardHeaderViewHolder.LAYOUT){
            abstractViewHolder = new DashboardHeaderViewHolder(view, mainView);
        } else if(viewType == EmptyDashboardViewHolder.LAYOUT){
            abstractViewHolder = new EmptyDashboardViewHolder(view, mainView);
        } else {
            abstractViewHolder = super.createViewHolder(view, viewType);
        }
        return abstractViewHolder;
    }
}
