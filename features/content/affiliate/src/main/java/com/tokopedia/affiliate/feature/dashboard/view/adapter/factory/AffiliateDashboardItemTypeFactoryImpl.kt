package com.tokopedia.affiliate.feature.dashboard.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.feature.dashboard.view.adapter.viewholder.AffiliateDashboardHeaderViewHolder
import com.tokopedia.affiliate.feature.dashboard.view.listener.AffiliateDashboardContract
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.DashboardHeaderViewModel

/**
 * Created by jegul on 2019-09-02.
 */
class AffiliateDashboardItemTypeFactoryImpl(val mainView: AffiliateDashboardContract.View) : BaseAdapterTypeFactory(), AffiliateDashboardItemTypeFactory {

    override fun type(dashboardInfoViewModel: DashboardHeaderViewModel): Int {
        return AffiliateDashboardHeaderViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            AffiliateDashboardHeaderViewHolder.LAYOUT -> AffiliateDashboardHeaderViewHolder(parent, mainView)
            else -> super.createViewHolder(parent, type)
        }
    }
}