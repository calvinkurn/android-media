package com.tokopedia.affiliate.feature.dashboard.view.adapter.factory

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.DashboardHeaderViewModel

/**
 * Created by jegul on 2019-09-02.
 */
interface AffiliateDashboardItemTypeFactory : AdapterTypeFactory {

    fun type(dashboardInfoViewModel: DashboardHeaderViewModel): Int
}