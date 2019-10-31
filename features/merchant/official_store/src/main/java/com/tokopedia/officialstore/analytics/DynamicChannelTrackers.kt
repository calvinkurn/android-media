package com.tokopedia.officialstore.analytics

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Banner
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Grid
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.ContextAnalytics

internal class DynamicChannelTrackers constructor(
        private val tracker: ContextAnalytics = TrackApp.getInstance().gtm
) {

    private val eventKey = "event"
    private val eventCategoryKey = "eventCategory"
    private val eventActionKey = "eventAction"
    private val eventLabelKey = "eventLabel"
    private val ecommerceKey = "ecommerce"

    fun flashSaleActionTextClick(categoryName: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                eventKey, "clickOSMicrosite",
                eventCategoryKey, "os microsite - $categoryName",
                eventActionKey, "flash sale - click",
                eventLabelKey, "click view all"
        ))
    }

    fun flashSalePDPClick(categoryName: String, headerName: String, position: String, gridData: Grid) {
        val ecommerceBody = DataLayer.mapOf(
                "click", DataLayer.mapOf(
                    "actionField", DataLayer.mapOf("list", "/official-store/$categoryName - flash sale - $headerName"),
                    "products", DataLayer.listOf(DataLayer.mapOf(
                        "name", gridData.name,
                        "id", gridData.id,
                        "price", gridData.price,
                        "brand", "none",
                        "category", "",
                        "variant", "none",
                        "list", "/official-store/$categoryName - flash sale - $headerName",
                        "position", position,
                        "attribution", gridData.attribution
                ))
            )
        )

        tracker.sendEnhanceEcommerceEvent(DataLayer.mapOf(
                eventKey, "productClick",
                eventCategoryKey, "os microsite - $categoryName",
                eventActionKey, "flash sale - product click",
                eventLabelKey, "click product picture - $headerName",
                ecommerceKey, ecommerceBody
        ))
    }

    fun dynamicChannelImageClick(categoryName: String, headerName: String, position: String, gridData: Grid) {
        val ecommerceBody = DataLayer.mapOf(
                "promoClick", DataLayer.mapOf(
                    "promotions", DataLayer.listOf(DataLayer.mapOf(
                        "id", gridData.id,
                        "name", "/official-store/$categoryName - dynamic channel - $headerName",
                        "position", position,
                        "creative", gridData.name,
                        "creative_url", gridData.imageUrl,
                        "promo_id", null,
                        "promo_code", null
                ))
            )
        )

        tracker.sendEnhanceEcommerceEvent(DataLayer.mapOf(
                eventKey, "promoClick",
                eventCategoryKey, "os microsite - $categoryName",
                eventActionKey, "dynamic channel - click",
                eventLabelKey, "click dynamic channel - $headerName",
                ecommerceKey, ecommerceBody
        ))
    }

    fun dynamicChannelMixCardClick(categoryName: String, headerName: String, position: String, gridData: Grid) {
        val ecommerceBody = DataLayer.mapOf(
                "click", DataLayer.mapOf(
                    "actionField", DataLayer.mapOf("list", "/official-store/$categoryName - dynamic channel mix - $headerName"),
                    "products", DataLayer.listOf(DataLayer.mapOf(
                        "name", gridData.name,
                        "id", gridData.id,
                        "price", gridData.price,
                        "brand", "none",
                        "category", "",
                        "variant", "none",
                        "list", "/official-store/$categoryName - dynamic channel mix - $headerName",
                        "position", position,
                        "attribution", gridData.attribution
                ))
            )
        )

        tracker.sendEnhanceEcommerceEvent(DataLayer.mapOf(
                eventKey, "productClick",
                eventCategoryKey, "os microsite - $categoryName",
                eventActionKey, "dynamic channel mix - product click",
                eventLabelKey, "click product picture - $headerName",
                ecommerceKey, ecommerceBody
        ))
    }

    fun dynamicChannelMixBannerClick(categoryName: String, headerName: String, position: String, bannerData: Banner) {
        val ecommerceBody = DataLayer.mapOf(
                "promoClick", DataLayer.mapOf(
                    "promotions", DataLayer.listOf(DataLayer.mapOf(
                        "id", bannerData.id,
                        "name", "/official-store/$categoryName - dynamic channel mix - $headerName",
                        "position", position,
                        "creative", bannerData.title,
                        "creative_url", bannerData.imageUrl,
                        "promo_id", null,
                        "promo_code", null
                ))
            )
        )

        tracker.sendEnhanceEcommerceEvent(DataLayer.mapOf(
                eventKey, "promoClick",
                eventCategoryKey, "os microsite - $categoryName",
                eventActionKey, "dynamic channel mix - banner click",
                eventLabelKey, "click banner dc mix - $headerName",
                ecommerceKey, ecommerceBody
        ))
    }
}
