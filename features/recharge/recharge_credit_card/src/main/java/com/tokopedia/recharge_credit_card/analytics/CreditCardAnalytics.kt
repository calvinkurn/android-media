package com.tokopedia.recharge_credit_card.analytics

import com.tokopedia.iris.Iris
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

class CreditCardAnalytics(val iris: Iris) {

    fun impressionInitialPage(categoryId: String, operatorId: String, userId: String) {
        if (iris != null) {
            val map = TrackAppUtils.gtmData(
                    EVENT_CC_IRIS,
                    CATEGORY_CC,
                    ACTION_IMPRESSION_INITIAL,
                    "$categoryId - $operatorId")
            map[USER_ID] = userId
            iris.saveEvent(map)
        }
    }

    fun impressionBankList(categoryId: String, operatorId: String, userId: String) {
        if (iris != null) {
            val map = TrackAppUtils.gtmData(
                    EVENT_CC_IRIS,
                    CATEGORY_CC,
                    ACTION_IMPRESSION_BANKLIST,
                    "$categoryId - $operatorId")
            map[USER_ID] = userId
            iris.saveEvent(map)
        }
    }

    fun clickToConfirmationPage(categoryId: String, operatorId: String, userId: String) {
        val map = TrackAppUtils.gtmData(
                EVENT_CC,
                CATEGORY_CC,
                ACTION_TO_SHOW_CONFIRMATION,
                "$categoryId - $operatorId")
        map[USER_ID] = userId
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun clickBackOnConfirmationPage(categoryId: String, operatorId: String, userId: String) {
        val map = TrackAppUtils.gtmData(
                EVENT_CC,
                CATEGORY_CC,
                ACTION_CLICK_BACK_CONFIRMATION,
                "$categoryId - $operatorId")
        map[USER_ID] = userId
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun clickToContinueCheckout(categoryId: String, operatorId: String, userId: String) {
        val map = TrackAppUtils.gtmData(
                EVENT_CC,
                CATEGORY_CC,
                ACTION_CLICK_CHECKOUT,
                "$categoryId - $operatorId")
        map[USER_ID] = userId
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    companion object {

        const val USER_ID = "userId"

        const val CATEGORY_CC = "digital - cc page"

        const val EVENT_CC = "clickDigitalCC"
        const val EVENT_CC_IRIS = "viewDigitalCCIris"

        const val ACTION_IMPRESSION_INITIAL = "impression of initial cc page"
        const val ACTION_IMPRESSION_BANKLIST = "impression of bank list"
        const val ACTION_CLICK_CHECKOUT = "checkout page"
        const val ACTION_CLICK_BACK_CONFIRMATION = "click confirmation to pdp"
        const val ACTION_TO_SHOW_CONFIRMATION = "click confirmation to checkout"

    }
}