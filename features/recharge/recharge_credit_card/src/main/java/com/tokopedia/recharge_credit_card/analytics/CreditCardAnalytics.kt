package com.tokopedia.recharge_credit_card.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

class CreditCardAnalytics {

    fun impressionInitialPage(categoryId: String, operatorId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                "",
                CATEGORY_CC,
                ACTION_IMPRESSION_INITIAL,
                "$categoryId - $operatorId"
        ))
    }

    fun impressionBankList(categoryId: String, operatorId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                "",
                CATEGORY_CC,
                ACTION_IMPRESSION_BANKLIST,
                "$categoryId - $operatorId"
        ))
    }

    fun clickToConfirmationPage(categoryId: String, operatorId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                "",
                CATEGORY_CC,
                ACTION_TO_SHOW_CONFIRMATION,
                "$categoryId - $operatorId"
        ))
    }

    fun clickBackOnConfirmationPage(categoryId: String, operatorId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                "",
                CATEGORY_CC,
                ACTION_CLICK_BACK_CONFIRMATION,
                "$categoryId - $operatorId"
        ))
    }

    fun clickToContinueCheckout(categoryId: String, operatorId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                "",
                CATEGORY_CC,
                ACTION_CLICK_CHECKOUT,
                "$categoryId - $operatorId"
        ))
    }

    companion object {

        const val CATEGORY_CC = "digital - cc page"

        const val ACTION_IMPRESSION_INITIAL = "impression of initial cc page"
        const val ACTION_IMPRESSION_BANKLIST = "impression of bank list"
        const val ACTION_CLICK_CHECKOUT = "checkout page"
        const val ACTION_CLICK_BACK_CONFIRMATION = "click confirmation to pdp"
        const val ACTION_TO_SHOW_CONFIRMATION = "click confirmation to checkout"

    }
}