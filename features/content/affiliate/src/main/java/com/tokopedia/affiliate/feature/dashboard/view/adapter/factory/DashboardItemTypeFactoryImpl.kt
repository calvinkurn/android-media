package com.tokopedia.affiliate.feature.dashboard.view.adapter.factory

import android.view.View

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.feature.dashboard.view.adapter.viewholder.DashboardHeaderViewHolder
import com.tokopedia.affiliate.feature.dashboard.view.adapter.viewholder.DashboardItemViewHolder
import com.tokopedia.affiliate.feature.dashboard.view.adapter.viewholder.EmptyDashboardViewHolder
import com.tokopedia.affiliate.feature.dashboard.view.listener.DashboardContract
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.DashboardHeaderViewModel
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.DashboardItemViewModel
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.EmptyDashboardViewModel

/**
 * @author by yfsx on 18/09/18.
 */
class DashboardItemTypeFactoryImpl(private val listener: OnClickListener) : BaseAdapterTypeFactory(), DashboardItemTypeFactory {

    private lateinit var mainView: DashboardContract.View

    constructor(mainView: DashboardContract.View): this(object : OnClickListener {
        override fun onDashboardItemClickedListener(item: DashboardItemViewModel) {
            mainView.onItemClicked(item)
        }
    }) {
        this.mainView = mainView
    }

    interface OnClickListener {
        fun onDashboardItemClickedListener(item: DashboardItemViewModel)
    }

    override fun type(dashboardItemViewModel: DashboardItemViewModel): Int {
        return DashboardItemViewHolder.LAYOUT
    }

    override fun type(dashboardInfoViewModel: DashboardHeaderViewModel): Int {
        return DashboardHeaderViewHolder.LAYOUT
    }


    override fun type(emptyDashboardViewModel: EmptyDashboardViewModel): Int {
        return EmptyDashboardViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*> {
        val abstractViewHolder: AbstractViewHolder<*>
        if (viewType == DashboardItemViewHolder.LAYOUT) {
            abstractViewHolder = DashboardItemViewHolder(view, listener::onDashboardItemClickedListener)
        } else if (viewType == DashboardHeaderViewHolder.LAYOUT) {
            abstractViewHolder = DashboardHeaderViewHolder(view, mainView)
        } else if (viewType == EmptyDashboardViewHolder.LAYOUT) {
            abstractViewHolder = EmptyDashboardViewHolder(view, mainView)
        } else {
            abstractViewHolder = super.createViewHolder(view, viewType)
        }
        return abstractViewHolder
    }
}
