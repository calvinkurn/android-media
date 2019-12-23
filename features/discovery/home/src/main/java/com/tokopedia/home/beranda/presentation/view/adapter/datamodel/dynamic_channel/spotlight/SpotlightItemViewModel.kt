package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.spotlight

import com.google.android.gms.tagmanager.DataLayer

data class SpotlightItemViewModel(val id: Int, val title: String, val description: String,
                             val backgroundImageUrl: String, val tagName: String,
                             val tagNameHexcolor: String, val tagHexcolor: String,
                             val ctaText: String, val ctaTextHexcolor: String,
                             val url: String, val applink: String, private val promoName: String, var channeldId: String) {

    fun getEnhanceClickSpotlightHomePage(position: Int, channelId: String): Map<String, Any> {
        return DataLayer.mapOf(
            "event", "promoClick",
            "eventCategory", "homepage",
            "eventAction", "click on banner spotlight",
            "eventLabel", title,
            "channelId", channelId,
            "ecommerce", DataLayer.mapOf(
                "promoClick", DataLayer.mapOf(
                "promotions", DataLayer.listOf(
                DataLayer.mapOf(
                        "id", id.toString(),
                        "name", promoName,
                        "position", (position + 1).toString(),
                        "creative", title,
                        "creative_url", backgroundImageUrl
                )
        )
        )
        )
        )
    }
}
