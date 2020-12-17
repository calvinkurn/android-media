package com.tokopedia.buyerorder.list.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorder.detail.view.OrderListAnalytics
import com.tokopedia.buyerorder.list.view.adapter.viewholder.*
import com.tokopedia.buyerorder.list.view.adapter.viewmodel.*

class OrderListAdapterFactory(var orderListAnalytics: OrderListAnalytics, var listener: OrderListViewHolder.OnMenuItemListener?,
                              var cartListener: OrderListRecomListViewHolder.ActionListener,
                              var filterListener: EmptyStateMarketPlaceFilterViewHolder.ActionListener) : BaseAdapterTypeFactory(), OrderListTypeFactory {
    override fun type(uiModel: OrderListUiModel): Int {
        return OrderListViewHolder.LAYOUT
    }

    override fun type(uiModel: OrderListRecomTitleUiModel): Int {
        return OrderListRecomTitleViewHolder.LAYOUT
    }

    override fun type(uiModel: EmptyStateOrderListUiModel): Int {
        return EmptyStateOrderListViewHolder.LAYOUT
    }

    override fun type(uiModel: EmptyStateMarketPlaceFilterUiModel): Int {
        return EmptyStateMarketPlaceFilterViewHolder.LAYOUT
    }

    override fun type(uiModel: EmptyStateMarketplaceUiModel): Int {
        return EmptyStateMarketplaceViewHolder.LAYOUT
    }

    override fun type(uiModel: OrderListRecomUiModel): Int {
        return OrderListRecomListViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            OrderListViewHolder.LAYOUT -> OrderListViewHolder(parent, orderListAnalytics, listener)
            OrderListRecomListViewHolder.LAYOUT -> OrderListRecomListViewHolder(parent, orderListAnalytics, cartListener)
            OrderListRecomTitleViewHolder.LAYOUT -> OrderListRecomTitleViewHolder(parent)
            EmptyStateMarketplaceViewHolder.LAYOUT -> EmptyStateMarketplaceViewHolder(parent)
            EmptyStateOrderListViewHolder.LAYOUT -> EmptyStateOrderListViewHolder(parent)
            EmptyStateMarketPlaceFilterViewHolder.LAYOUT -> EmptyStateMarketPlaceFilterViewHolder(parent,filterListener)
            else -> super.createViewHolder(parent, type)
        }
    }
}
