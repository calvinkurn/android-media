package com.tokopedia.normalcheckout.view

import com.appsflyer.AFInAppEventParameterName
import com.appsflyer.AFInAppEventType
import com.tokopedia.track.TrackApp

class NormalCheckoutTracking {
    companion object {
        const val CLICK_PDP = "clickPDP"
        const val PRODUCT_DETAIL_PAGE = "product detail page"
        const val SELECT_COLOR_VARIANT = "select color on variants page"
        const val SELECT_SIZE_VARIANT = "select size on variants page"
    }
    fun eventClickBuyInVariantNotLogin(productId: String?) {
        if (productId.isNullOrEmpty()) return
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(
            CLICK_PDP,
            PRODUCT_DETAIL_PAGE,
            "click - beli on variants page - before login",
            productId)
    }

    fun eventClickAtcInVariantNotLogin(productId: String?) {
        if (productId.isNullOrEmpty()) return
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(
            CLICK_PDP,
            PRODUCT_DETAIL_PAGE,
            "click - tambah ke keranjang on variants page - before login",
            productId)
    }

    fun eventSelectSizeVariant(size: String?) {
        if (size.isNullOrEmpty()) return
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(
            CLICK_PDP,
            PRODUCT_DETAIL_PAGE,
            SELECT_SIZE_VARIANT,
            size)
    }

    fun eventSelectColorVariant(color: String?) {
        if (color.isNullOrEmpty()) return
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(
            CLICK_PDP,
            PRODUCT_DETAIL_PAGE,
            SELECT_COLOR_VARIANT,
            color)
    }

    fun eventAppsFlyer(productId: String, priceItem: String,
                       quantity: Int,
                       productName: String,
                       category: String) {
        TrackApp.getInstance()?.appsFlyer?.sendEvent(
            AFInAppEventType.ADD_TO_CART,
            mutableMapOf<String, Any>(
                AFInAppEventParameterName.CONTENT_ID to productId,
                AFInAppEventParameterName.DESCRIPTION to productName,
                AFInAppEventParameterName.CURRENCY to "IDR",
                AFInAppEventParameterName.QUANTITY to quantity,
                AFInAppEventParameterName.PRICE to priceItem,
                "category" to category
            ))
    }
}
