package com.tokopedia.home.beranda.presentation.view.adapter.factory.balancewidget

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceShimmerModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceWidgetFailedModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.HomeBalanceModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.BalanceWidgetFailedViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.BalanceWidgetShimmerViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.BalanceWidgetViewHolder

/**
 * Created by dhaba
 */
class BalanceWidgetTypeFactoryImpl(val listener: HomeCategoryListener?) : BaseAdapterTypeFactory(), BalanceWidgetTypeFactory {
    private var balanceWidgetViewHolder: BalanceWidgetViewHolder ? = null

    override fun type(dataModel: HomeBalanceModel): Int {
        return BalanceWidgetViewHolder.LAYOUT
    }

    override fun type(dataModel: BalanceShimmerModel): Int {
        return BalanceWidgetShimmerViewHolder.LAYOUT
    }

    override fun type(dataModel: BalanceWidgetFailedModel): Int {
        return BalanceWidgetFailedViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            BalanceWidgetViewHolder.LAYOUT -> {
                balanceWidgetViewHolder = BalanceWidgetViewHolder(view, listener)
                balanceWidgetViewHolder
            }
            BalanceWidgetShimmerViewHolder.LAYOUT -> BalanceWidgetShimmerViewHolder(view, listener)
            BalanceWidgetFailedViewHolder.LAYOUT -> BalanceWidgetFailedViewHolder(view, listener)
            else -> super.createViewHolder(view, type)
        } as AbstractViewHolder<BalanceWidgetVisitable>
    }
}
