package com.tokopedia.recharge_credit_card.analytics

import com.tokopedia.iris.Iris
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

class CreditCardAnalytics(val iris: Iris) {

    fun impressionInitialPage(userId: String) {
        val map = TrackAppUtils.gtmData(
                EVENT_HOMEPAGE,
                CATEGORY_HOMEPAGE,
                ACTION_IMPRESSION_INITIAL,
                getCategoryName()
        )
        map[KEY_USER_ID] = userId
        map[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_RECHARGE
        map[KEY_CURRENT_SITE] = CURRENT_SITE_RECHARGE
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun impressionBankList(categoryId: String, operatorId: String, userId: String) {
        if (iris != null) {
            val map = TrackAppUtils.gtmData(
                    EVENT_CC_IRIS,
                    CATEGORY_CC,
                    ACTION_IMPRESSION_BANKLIST,
                    "$categoryId - $operatorId")
            map[KEY_USER_ID] = userId
            iris.saveEvent(map)
        }
    }

    fun clickToConfirmationPage(categoryId: String, operatorId: String, userId: String) {
        val map = TrackAppUtils.gtmData(
                EVENT_CC,
                CATEGORY_CC,
                ACTION_TO_SHOW_CONFIRMATION,
                "$categoryId - $operatorId")
        map[KEY_USER_ID] = userId
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun clickBackOnConfirmationPage(categoryId: String, operatorId: String, userId: String) {
        val map = TrackAppUtils.gtmData(
                EVENT_CC,
                CATEGORY_CC,
                ACTION_CLICK_BACK_CONFIRMATION,
                "$categoryId - $operatorId")
        map[KEY_USER_ID] = userId
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun clickToContinueCheckout(categoryId: String, operatorId: String, userId: String) {
        val map = TrackAppUtils.gtmData(
                EVENT_CC,
                CATEGORY_CC,
                ACTION_CLICK_CHECKOUT,
                "$categoryId - $operatorId")
        map[KEY_USER_ID] = userId
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    companion object {
        private fun getCategoryName(): String = "Tagihan Kartu Kredit"

        const val KEY_USER_ID = "userId"
        const val KEY_BUSINESS_UNIT = "businessUnit"
        const val KEY_CURRENT_SITE = "currentSite"

        const val CATEGORY_CC = "digital - cc page"
        const val CATEGORY_HOMEPAGE = "digital - homepage"

        const val EVENT_CC = "clickDigitalCC"
        const val EVENT_CC_IRIS = "viewDigitalCCIris"
        const val EVENT_HOMEPAGE = "viewHomepage"

        const val ACTION_IMPRESSION_INITIAL = "view pdp page"
        const val ACTION_IMPRESSION_BANKLIST = "impression of bank list"
        const val ACTION_CLICK_CHECKOUT = "checkout page"
        const val ACTION_CLICK_BACK_CONFIRMATION = "click confirmation to pdp"
        const val ACTION_TO_SHOW_CONFIRMATION = "click confirmation to checkout"

        const val BUSINESS_UNIT_RECHARGE = "recharge"
        const val CURRENT_SITE_RECHARGE = "tokopediadigital"

    }
}