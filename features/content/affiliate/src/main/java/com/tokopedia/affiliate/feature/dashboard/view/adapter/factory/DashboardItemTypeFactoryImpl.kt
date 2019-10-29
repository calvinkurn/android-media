package com.tokopedia.affiliate.feature.dashboard.view.adapter.factory

import android.view.View

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.feature.dashboard.view.adapter.viewholder.DashboardItemViewHolder
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.DashboardItemViewModel

/**
 * @author by yfsx on 18/09/18.
 */
class DashboardItemTypeFactoryImpl(private val listener: OnClickListener) : BaseAdapterTypeFactory(), DashboardItemTypeFactory {

    interface OnClickListener {
        fun onDashboardItemClickedListener(item: DashboardItemViewModel)
        fun onBuyClick(appLink: String)
    }

    override fun type(dashboardItemViewModel: DashboardItemViewModel): Int {
        return DashboardItemViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*> {
        return when (viewType) {
            DashboardItemViewHolder.LAYOUT -> DashboardItemViewHolder(view, listener::onDashboardItemClickedListener, listener::onBuyClick)
            else -> super.createViewHolder(view, viewType)
        }
    }
}
