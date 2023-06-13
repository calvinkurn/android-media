package com.tokopedia.attachproduct.analytics

import android.os.Bundle
import com.tokopedia.attachcommon.data.ResultProduct
import com.tokopedia.attachproduct.view.tracking.AttachProductEventTracking
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.TrackApp

object AttachProductAnalytics {
    val eventCheckProduct: AttachProductEventTracking
        get() = AttachProductEventTracking(
            Event.CLICK_CHAT_DETAIL,
            Category.CHAT_DETAIL,
            Action.CHECK_PRODUCT,
            ""
        )

    fun getEventCheckProductTalk(productId: String): AttachProductEventTracking {
        return AttachProductEventTracking(
            "clickInboxChat",
            "inbox - talk",
            "attach product",
            productId
        )
    }

    private fun getProductIdList(bundleItems: List<ResultProduct>): List<String> {
        val productIdList = mutableListOf<String>()
        for (item in bundleItems) {
            productIdList += item.productId.toString()
        }
        return productIdList
    }

    fun trackSendButtonClicked(
        products: List<ResultProduct>
    ) {
        val productIds = getProductIdList(products)
        val eventDataLayer = Bundle()
        eventDataLayer.putString(TrackAppUtils.EVENT, Event.CLICK_COMMUNICATION)
        eventDataLayer.putString(
            TrackAppUtils.EVENT_ACTION,
            Action.CLICK_KIRIM_AFTER_PILIH_PRODUCT_VARIANT
        )
        eventDataLayer.putString(TrackAppUtils.EVENT_CATEGORY, Category.CHAT_DETAIL)
        eventDataLayer.putString(TrackAppUtils.EVENT_LABEL, productIds.joinToString())
        eventDataLayer.putString(TRACKER_ID, "14823")
        eventDataLayer.putString(KEY_BUSINESS_UNIT, COMMUNICATION)
        eventDataLayer.putString(KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA)

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            Event.CLICK_COMMUNICATION,
            eventDataLayer
        )
    }

    val eventClickChatAttachedProductImage: AttachProductEventTracking
        get() {
            return AttachProductEventTracking(
                Event.CLICK_CHAT_DETAIL,
                Category.CHAT_DETAIL,
                Action.CLICK_PRODUCT_IMAGE,
                ""
            )
        }

    object Event {
        const val CLICK_CHAT_DETAIL: String = "ClickChatDetail"
        const val CLICK_COMMUNICATION = "clickCommunication"
    }

    object Category {
        const val CHAT_DETAIL: String = "chat detail"
    }

    object Action {
        const val CLICK_PRODUCT_IMAGE: String = "click on product image"
        const val CHECK_PRODUCT: String = "click one of the product"
        const val CLICK_KIRIM_AFTER_PILIH_PRODUCT_VARIANT =
            "click kirim after pilih product variant"
    }

    private const val TRACKER_ID = "trackerId"

    //General Keys
    private const val KEY_BUSINESS_UNIT = "businessUnit"
    private const val KEY_CURRENT_SITE = "currentSite"

    //Other
    private const val CURRENT_SITE = "attachproduct"
    private const val COMMUNICATION_MEDIA = "Communication & Media"
    private const val COMMUNICATION = "communication"
    private const val CURRENT_SITE_TOKOPEDIA = "tokopediamarketplace"
}
