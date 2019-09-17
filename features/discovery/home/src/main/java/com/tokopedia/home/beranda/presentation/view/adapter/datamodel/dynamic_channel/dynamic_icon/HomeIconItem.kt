package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.dynamic_icon

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.kotlin.model.ImpressHolder

/**
 * @author by errysuprayogi on 11/28/17.
 */
class HomeIconItem(var id: String?, var title: String, var icon: String, var applink: String, var url: String) : ImpressHolder() {

    fun getEnhanceClickDynamicIconHomePage(position: Int): Map<String, Any> {
        return DataLayer.mapOf(
                "event", "promoClick",
                "eventCategory", "homepage",
                "eventAction", "click on dynamic icon",
                "eventLabel", title,
                "ecommerce", DataLayer.mapOf(
                "promoClick", DataLayer.mapOf(
                "promotions", DataLayer.listOf(
                DataLayer.mapOf(
                        "id", id,
                        "name", "/ - dynamic icon",
                        "position", (position + 1).toString(),
                        "creative", title,
                        "creative_url", icon
                )
        )
        )
        )
        )
    }
}
