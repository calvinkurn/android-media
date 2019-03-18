package com.tokopedia.normalcheckout.view

import com.appsflyer.AFInAppEventParameterName
import com.appsflyer.AFInAppEventType
import com.tokopedia.track.TrackApp
import java.util.HashMap

class NormalCheckoutTracking {
    companion object {
        const val CLICK_PDP = "clickPDP"
        const val PRODUCT_DETAIL_PAGE = "product detail page"
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

    fun eventAppsFlyer(productId: String, priceItem: String,
                       quantity: Int,
                       productName: String,
                       category: String) {
        TrackApp.getInstance()?.appsFlyer?.sendTrackEvent(
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
