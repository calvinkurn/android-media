package com.tokopedia.buyerorder.list.view.adapter.factory

import com.tokopedia.buyerorder.list.view.adapter.viewmodel.*

interface OrderListTypeFactory {

    fun type(viewModel: OrderListRecomViewModel): Int

    fun type(viewModel: EmptyStateMarketplaceViewModel): Int

    fun type(viewModel: EmptyStateOrderListViewModel): Int

    fun type(viewModel: EmptyStateMarketPlaceFilterViewModel): Int

    fun type(viewModel: OrderListRecomTitleViewModel): Int

    fun type(viewModel: OrderListViewModel): Int
}
