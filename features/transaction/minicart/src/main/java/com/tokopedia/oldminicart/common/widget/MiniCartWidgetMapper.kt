package com.tokopedia.oldminicart.common.widget

import com.tokopedia.minicart.common.analytics.MiniCartAnalytics
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartItemKey
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.MiniCartWidgetData
import com.tokopedia.minicart.common.widget.GlobalEvent

object MiniCartWidgetMapper {

    fun mapToOldMiniCartData(miniCartSimplifiedData: MiniCartSimplifiedData): com.tokopedia.oldminicart.common.domain.data.MiniCartSimplifiedData {
        val widgetData = miniCartSimplifiedData.miniCartWidgetData
        val miniCartWidgetData = com.tokopedia.oldminicart.common.domain.data.MiniCartWidgetData(
            widgetData.totalProductCount,
            widgetData.totalProductPrice,
            widgetData.totalProductError,
            widgetData.containsOnlyUnavailableItems,
            widgetData.unavailableItemsCount,
            widgetData.isOCCFlow,
            widgetData.buttonBuyWording
        )
        val cartItems = miniCartSimplifiedData.miniCartItems.values.filterIsInstance<MiniCartItem.MiniCartItemProduct>().map {
            com.tokopedia.oldminicart.common.domain.data.MiniCartItem(
                isError = it.isError,
                cartId = it.cartId,
                productId = it.productId,
                productParentId = it.productParentId,
                quantity = it.quantity,
                notes = it.notes,
                cartString = it.cartString,
                campaignId = it.campaignId,
                attribution = it.attribution,
                productWeight = it.productWeight,
                productSlashPriceLabel = it.productSlashPriceLabel,
                warehouseId = it.warehouseId,
                shopId = it.shopId,
                shopName = it.shopName,
                shopType = it.shopType,
                categoryId = it.categoryId,
                freeShippingType = it.freeShippingType,
                category = it.category,
                productName = it.productName,
                productVariantName = it.productVariantName,
                productPrice = it.productPrice
            )
        }
        val isShowMiniCartWidget = miniCartSimplifiedData.isShowMiniCartWidget

        return com.tokopedia.oldminicart.common.domain.data.MiniCartSimplifiedData(
            miniCartWidgetData,
            cartItems,
            isShowMiniCartWidget
        )
    }

    fun mapToMiniCartData(miniCartSimplifiedData: com.tokopedia.oldminicart.common.domain.data.MiniCartSimplifiedData): MiniCartSimplifiedData {
        val widgetData = miniCartSimplifiedData.miniCartWidgetData
        val miniCartWidgetData = MiniCartWidgetData(
            widgetData.totalProductCount,
            widgetData.totalProductPrice,
            widgetData.totalProductError,
            widgetData.containsOnlyUnavailableItems,
            widgetData.unavailableItemsCount,
            widgetData.isOCCFlow,
            widgetData.buttonBuyWording
        )

        val cartItems = mutableMapOf<MiniCartItemKey, MiniCartItem>()

        miniCartSimplifiedData.miniCartItems.forEach {
            val item = MiniCartItem.MiniCartItemProduct(
                isError = it.isError,
                cartId = it.cartId,
                productId = it.productId,
                productParentId = it.productParentId,
                quantity = it.quantity,
                notes = it.notes,
                cartString = it.cartString,
                campaignId = it.campaignId,
                attribution = it.attribution,
                productWeight = it.productWeight,
                productSlashPriceLabel = it.productSlashPriceLabel,
                warehouseId = it.warehouseId,
                shopId = it.shopId,
                shopName = it.shopName,
                shopType = it.shopType,
                categoryId = it.categoryId,
                freeShippingType = it.freeShippingType,
                category = it.category,
                productName = it.productName,
                productVariantName = it.productVariantName,
                productPrice = it.productPrice
            )
            val key = MiniCartItemKey(it.productId)
            cartItems[key] = item
        }
        val isShowMiniCartWidget = miniCartSimplifiedData.isShowMiniCartWidget

        return MiniCartSimplifiedData(
            miniCartWidgetData,
            cartItems,
            isShowMiniCartWidget
        )
    }
    
    fun mapToOldGlobalEvent(globalEvent: GlobalEvent): com.tokopedia.oldminicart.common.widget.GlobalEvent {
        return com.tokopedia.oldminicart.common.widget.GlobalEvent(
            globalEvent.observer,
            globalEvent.state,
            globalEvent.data,
            globalEvent.throwable
        )
    }

    fun mapToOldPageName(pageName: MiniCartAnalytics.Page): com.tokopedia.oldminicart.common.analytics.MiniCartAnalytics.Page {
        return when (pageName) {
            MiniCartAnalytics.Page.HOME_PAGE -> com.tokopedia.oldminicart.common.analytics.MiniCartAnalytics.Page.HOME_PAGE
            MiniCartAnalytics.Page.SEARCH_PAGE -> com.tokopedia.oldminicart.common.analytics.MiniCartAnalytics.Page.SEARCH_PAGE
            MiniCartAnalytics.Page.CATEGORY_PAGE -> com.tokopedia.oldminicart.common.analytics.MiniCartAnalytics.Page.CATEGORY_PAGE
            MiniCartAnalytics.Page.DISCOVERY_PAGE -> com.tokopedia.oldminicart.common.analytics.MiniCartAnalytics.Page.DISCOVERY_PAGE
            MiniCartAnalytics.Page.RECOMMENDATION_INFINITE -> com.tokopedia.oldminicart.common.analytics.MiniCartAnalytics.Page.RECOMMENDATION_INFINITE
            MiniCartAnalytics.Page.MVC_PAGE -> com.tokopedia.oldminicart.common.analytics.MiniCartAnalytics.Page.MVC_PAGE
        }
    }
}