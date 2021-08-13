package com.tokopedia.buyerorder.list.view.adapter.factory

import com.tokopedia.buyerorder.list.view.adapter.viewmodel.*

interface OrderListTypeFactory {

    fun type(uiModel: OrderListRecomUiModel): Int

    fun type(uiModel: EmptyStateMarketplaceUiModel): Int

    fun type(uiModel: EmptyStateOrderListUiModel): Int

    fun type(uiModel: EmptyStateMarketPlaceFilterUiModel): Int

    fun type(uiModel: OrderListRecomTitleUiModel): Int

    fun type(uiModel: OrderListUiModel): Int
}
