package com.tokopedia.promousage.analytics

import com.google.gson.annotations.SerializedName
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.Tracker

class PromoBenefitAnalytics {

    private val gtm by lazy { TrackApp.getInstance().gtm }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4436
    // Tracker ID: 49314
    fun sendImpressionPromoDetailBottomSheetEvent(
        businessUnit: String,
        currentSite: String,
        promotions: List<PromotionModel>,
        userId: String
    ) {
        Tracker.Builder()
            .setEvent("view_item")
            .setEventAction("impression - promo detail bottom sheet")
            .setEventCategory("product detail page")
            .setEventLabel("")
            .setCustomProperty("trackerId", "49314")
            .setBusinessUnit(businessUnit)
            .setCurrentSite(currentSite)
            .setCustomProperty("promotions", promotions.toMap())
            .setUserId(userId)
            .build()
            .send()
    }

    data class PromotionModel(
        @SerializedName("creative_name") val creativeName: String,
        @SerializedName("creative_slot") val creativeSlot: String,
        @SerializedName("item_id") val itemId: String,
        @SerializedName("item_name") val itemName: String
    )

    private fun List<PromotionModel>.toMap() = map {
        mapOf(
            "creative_name" to it.creativeName,
            "creative_slot" to it.creativeSlot,
            "item_id" to it.itemId,
            "item_name" to it.itemName
        )
    }
}
