package com.tokopedia.affiliate.feature.dashboard.view.adapter.factory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.DashboardHeaderViewModel;
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.DashboardItemViewModel;
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.EmptyDashboardViewModel;

/**
 * @author by yfsx on 18/09/18.
 */
public interface DashboardItemTypeFactory extends AdapterTypeFactory {

    int type(DashboardHeaderViewModel dashboardInfoViewModel);

    int type(DashboardItemViewModel dashboardItemViewModel);

    int type(EmptyDashboardViewModel emptyDashboardViewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);
}
