package com.tokopedia.buyerorder.list.view.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.buyerorder.list.view.adapter.factory.OrderListAdapterFactory
import com.tokopedia.buyerorder.list.view.adapter.viewmodel.EmptyStateMarketPlaceFilterViewModel
import com.tokopedia.buyerorder.list.view.adapter.viewmodel.EmptyStateMarketplaceViewModel
import com.tokopedia.buyerorder.list.view.adapter.viewmodel.EmptyStateOrderListViewModel

class OrderListAdapter(orderListAdapterFactory: OrderListAdapterFactory)
    : BaseAdapter<OrderListAdapterFactory>(orderListAdapterFactory) {

    private val emptyStateMarketPlaceViewModel = EmptyStateMarketplaceViewModel()
    private val emptyStateOrderListViewModel = EmptyStateOrderListViewModel()
    private val emptyStateMarketPlaceFilterViewModel = EmptyStateMarketPlaceFilterViewModel()

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