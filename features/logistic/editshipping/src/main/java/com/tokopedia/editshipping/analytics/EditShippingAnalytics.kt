package com.tokopedia.editshipping.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.Analytics

object EditShippingAnalytics {

    private const val CLICK_TEXT_DISINI = "clickBebasOngkir"
    private const val BO_SHIPPING_EDITOR = "bo shipping editor"
    private const val CLICK_DISINI = "click disini"
    private const val CLICK_CREATE_SHOP = "clickCreateShop"
    private const val CREATE_SHOP = "Create Shop"
    private const val CLICK = "Click"
    private const val SAVE_LOGISTIC_ERROR = "Save Logistic Error"

    private const val KEY_USER_ID = "userId"

    private fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }

    private fun sendEventCategoryAction(
        event: String,
        eventCategory: String,
        eventAction: String
    ) {
        sendEventCategoryActionLabel(event, eventCategory, eventAction, "")
    }

    private fun sendEventCategoryActionLabel(
        event: String,
        eventCategory: String,
        eventAction: String,
        eventLabel: String
    ) {
        getTracker().sendGeneralEvent(
            TrackAppUtils.gtmData(
                event,
                eventCategory,
                eventAction,
                eventLabel
            )
        )
    }

    @JvmStatic
    fun eventCreateShopFillLogisticError() {
        sendEventCategoryActionLabel(CLICK_CREATE_SHOP, CREATE_SHOP, CLICK, SAVE_LOGISTIC_ERROR)
    }

    @JvmStatic
    fun eventClickonTickerShippingEditor(userId: String) {
        val map = TrackAppUtils.gtmData(
            CLICK_TEXT_DISINI,
            BO_SHIPPING_EDITOR,
            CLICK_DISINI,
            ""
        )
        map[KEY_USER_ID] = userId
        getTracker().sendGeneralEvent(map)
    }
}
