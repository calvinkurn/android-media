package com.tokopedia.buyerorder.list.view.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.buyerorder.list.view.adapter.factory.OrderListAdapterFactory
import com.tokopedia.buyerorder.list.view.adapter.viewmodel.EmptyStateMarketPlaceFilterUiModel
import com.tokopedia.buyerorder.list.view.adapter.viewmodel.EmptyStateMarketplaceUiModel
import com.tokopedia.buyerorder.list.view.adapter.viewmodel.EmptyStateOrderListUiModel

class OrderListAdapter(orderListAdapterFactory: OrderListAdapterFactory)
    : BaseAdapter<OrderListAdapterFactory>(orderListAdapterFactory) {

    private val emptyStateMarketPlaceViewModel = EmptyStateMarketplaceUiModel()
    private val emptyStateOrderListViewModel = EmptyStateOrderListUiModel()
    private val emptyStateMarketPlaceFilterViewModel = EmptyStateMarketPlaceFilterUiModel()

    fun setEmptyMarketplace() {
        this.visitables.clear()
        addElement(emptyStateMarketPlaceViewModel)
    }
    fun setEmptyMarketplaceFilter() {
        this.visitables.clear()
        addElement(emptyStateMarketPlaceFilterViewModel)
    }
    fun setEmptyOrderList() {
        this.visitables.clear()
        addElement(emptyStateOrderListViewModel)
    }

}