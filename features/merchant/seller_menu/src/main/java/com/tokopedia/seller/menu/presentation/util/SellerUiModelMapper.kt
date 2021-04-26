package com.tokopedia.seller.menu.presentation.util

import com.tokopedia.gm.common.constant.COMMUNICATION_PERIOD
import com.tokopedia.gm.common.presentation.model.ShopInfoPeriodUiModel
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.manage.common.feature.list.data.model.filter.Tab
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

    fun mapToProductUiModel(response: List<Tab>, isShopOwner: Boolean): ShopProductUiModel {
        var totalProductCount = 0

        response.filter{ supportedProductStatus.contains(it.id) }.map {
            totalProductCount += it.value.toIntOrZero()
        }

        return ShopProductUiModel(totalProductCount, isShopOwner)
    }

    fun mapToNotificationUiModel(response: SellerMenuNotificationResponse, isShopOwner: Boolean): NotificationUiModel {
        val notifications = response.notifications
        val sellerOrder = notifications.sellerOrderStatus
        val shopOrderUiModel = ShopOrderUiModel(sellerOrder.newOrder, sellerOrder.readyToShip, isShopOwner)

        return NotificationUiModel(
            notifications.inbox.talk,
            notifications.notifCenterTotalUnread.seller,
            shopOrderUiModel,
            response.notifications.sellerOrderStatus.inResolution
        )
    }

    fun mapToIsShowTickerShopAccount(shopInfoPeriodUiModel: ShopInfoPeriodUiModel): Boolean {
        return shopInfoPeriodUiModel.periodType == COMMUNICATION_PERIOD
    }
}