package com.tokopedia.seller.menu.presentation.util

import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.manage.common.list.data.model.filter.Tab
import com.tokopedia.seller.menu.common.view.uimodel.ShopOrderUiModel
import com.tokopedia.seller.menu.common.view.uimodel.ShopProductUiModel
import com.tokopedia.seller.menu.data.model.SellerMenuNotificationResponse
import com.tokopedia.seller.menu.presentation.uimodel.NotificationUiModel
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus

object SellerUiModelMapper {

    private val supportedProductStatus = listOf(
        ProductStatus.ACTIVE.name,
        ProductStatus.INACTIVE.name,
        ProductStatus.VIOLATION.name
    )

    fun mapToProductUiModel(response: List<Tab>): ShopProductUiModel {
        var totalProductCount = 0

        response.filter{ supportedProductStatus.contains(it.id) }.map {
            totalProductCount += it.value.toIntOrZero()
        }

        return ShopProductUiModel(totalProductCount)
    }

    fun mapToNotificationUiModel(response: SellerMenuNotificationResponse): NotificationUiModel {
        val sellerOrder = response.notifications.sellerOrderStatus
        val shopOrderUiModel = ShopOrderUiModel(sellerOrder.newOrder, sellerOrder.readyToShip)

        return NotificationUiModel(
            response.inbox.talk,
            response.notifCenterTotalUnread.seller,
            shopOrderUiModel
        )
    }
}