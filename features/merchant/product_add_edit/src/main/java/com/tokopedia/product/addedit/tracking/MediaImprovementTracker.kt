package com.tokopedia.product.addedit.tracking

import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.Tracker
import com.tokopedia.track.interfaces.ContextAnalytics

object MediaImprovementTracker {

    const val ADD_PRODUCT_ENTRY_POINT = "add_product"
    const val EDIT_PRODUCT_ENTRY_POINT = "edit_product"
    const val ADD_VARIANT_ENTRY_POINT = "add_variant"
    const val EDIT_VARIANT_ENTRY_POINT = "edit_variant"

    fun getTracker(): ContextAnalytics {
        if (ProductAddEditTracking.gtmTracker == null) {
            ProductAddEditTracking.gtmTracker = TrackApp.getInstance().getGTM()
        }
        return ProductAddEditTracking.gtmTracker!!
    }

    fun sendProductActionTracker(isEdit: Boolean, userId: String, shopId: String){
        val label = if(isEdit) EDIT_PRODUCT_ENTRY_POINT else ADD_PRODUCT_ENTRY_POINT
        val eventLabel = "$label-$userId-$shopId"
        Tracker.Builder()
            .setEvent("clickCommunication")
            .setEventAction("click tambah")
            .setEventCategory("media product service")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "38949")
            .setBusinessUnit("media")
            .setCurrentSite("tokopediamarketplace")
            .setUserId(userId)
            .build()
            .send()

    }

    fun sendVariantActionTracker(isEdit: Boolean, userId: String, shopId: String){
        val label = if(isEdit) EDIT_VARIANT_ENTRY_POINT else ADD_VARIANT_ENTRY_POINT
        val eventLabel = "$label-$userId-$shopId"
        Tracker.Builder()
            .setEvent("clickCommunication")
            .setEventAction("click tambah")
            .setEventCategory("media product service")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "38949")
            .setBusinessUnit("media")
            .setCurrentSite("tokopediamarketplace")
            .setUserId(userId)
            .build()
            .send()

    }
}
