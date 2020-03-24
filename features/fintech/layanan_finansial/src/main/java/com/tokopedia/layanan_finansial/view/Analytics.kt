package com.tokopedia.layanan_finansial.view

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

object Analytics {
    const val ECOMERCE = "ecommerce"
    const val EVENT_PROMO_CLICK = "promoClick"
    const val EVENT_PROMO_VIEW = "promoView"
    const val LAYANAN_FINANSIAL_CATEGORY = "layanan finansial page"
    const val LAYANAN_FINANSILA_VIEW_ACTION = "view layanan finansial product"
    const val LAYANAN_FINANSILA_click_ACTION = "click layanan finansial product"

    fun sendEcomerceEvent(event: String, category: String, action: String, label: String,ecomerce : Map<String,Any?>){
        val map = TrackAppUtils.gtmData(event,category,action,label)
        map.put(ECOMERCE, ecomerce)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }
}