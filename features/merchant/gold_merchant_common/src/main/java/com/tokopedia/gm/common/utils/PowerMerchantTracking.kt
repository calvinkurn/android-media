package com.tokopedia.gm.common.utils

import com.tokopedia.gm.common.constant.GMParamTracker
import com.tokopedia.track.TrackApp
import javax.inject.Inject


class PowerMerchantTracking @Inject constructor() {

    fun eventUpgradeShopHome() {
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(
                GMParamTracker.EVENT_CLICK_HOME_PAGE,
                GMParamTracker.CATEGORY_HOME_PAGE,
                GMParamTracker.ACTION_CLICK_UPGRADE_SHOP,
                "")
    }

    fun eventLearnMorePm() {
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(
                GMParamTracker.EVENT_CLICK_POWER_MERCHANT,
                GMParamTracker.CATEGORY_SELLER_APP,
                GMParamTracker.ACTION_CLICK_POWER_MERCHANT,
                GMParamTracker.LABEL_FEATURES_LEARN_MORE)
    }


    fun eventUpgradeShopPm() {
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(
                GMParamTracker.EVENT_CLICK_POWER_MERCHANT,
                GMParamTracker.CATEGORY_SELLER_APP,
                GMParamTracker.ACTION_CLICK_POWER_MERCHANT,
                GMParamTracker.LABEL_UPGRADE_SHOP)
    }


    fun eventUpgradeShopWebView() {
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(
                GMParamTracker.EVENT_CLICK_POWER_MERCHANT,
                GMParamTracker.CATEGORY_SELLER_APP,
                GMParamTracker.ACTION_CLICK_POWER_MERCHANT,
                GMParamTracker.LABEL_TERMS_CONDITION)
    }

    fun eventLearnMoreSuccessPopUp() {
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(
                GMParamTracker.EVENT_CLICK_POWER_MERCHANT,
                GMParamTracker.CATEGORY_SELLER_APP,
                GMParamTracker.ACTION_CLICK_POWER_MERCHANT,
                GMParamTracker.LABEL_POP_UP_LEARN)
    }

    fun eventIncreaseScorePopUp() {
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(
                GMParamTracker.EVENT_CLICK_POWER_MERCHANT,
                GMParamTracker.CATEGORY_SELLER_APP,
                GMParamTracker.ACTION_POWER_MERCHANT_INCREASE_PERFORMANCE,
                "")
    }

    fun eventUpgradeShopSetting() {
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(
                GMParamTracker.EVENT_CLICK_SHOP_SETTING,
                GMParamTracker.CATEGORY_SELLER_APP,
                GMParamTracker.ACTION_CLICK_SHOP_SETTINGS,
                GMParamTracker.LABEL_SHOP_INFO)
    }

    fun eventLearnMoreSetting() {
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(
                GMParamTracker.EVENT_CLICK_SHOP_SETTING,
                GMParamTracker.CATEGORY_SELLER_APP,
                GMParamTracker.ACTION_CLICK_SHOP_SETTINGS,
                GMParamTracker.LABEL_SHOP_INFO_LEARN_MORE)
    }

    fun eventCancelMembershipPm() {
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(
                GMParamTracker.EVENT_CLICK_POWER_MERCHANT,
                GMParamTracker.CATEGORY_SELLER_APP,
                GMParamTracker.ACTION_CLICK_POWER_MERCHANT_PM,
                GMParamTracker.LABEL_CANCEL_MEMBERSHIP)
    }

    fun eventCancelMembershipBottomSheet() {
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(
                GMParamTracker.EVENT_CLICK_POWER_MERCHANT,
                GMParamTracker.CATEGORY_SELLER_APP,
                GMParamTracker.ACTION_CLICK_POWER_MERCHANT_PM,
                GMParamTracker.LABEL_POP_UP_CANCEL_MEMBERSHIP)
    }

    fun eventIncreaseScoreBottomSheet() {
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(
                GMParamTracker.EVENT_CLICK_HOME_PAGE,
                GMParamTracker.CATEGORY_HOME_PAGE,
                GMParamTracker.ACTION_CLICK_INCREASE_SHOP_PERFORMANCE,
                "")
    }

    fun eventPMCancellationQuestionnaireClickSendAnswer() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                GMParamTracker.EVENT_CLICK_POWER_MERCHANT,
                GMParamTracker.Category.PM_QUESTIONNAIRE,
                GMParamTracker.Action.CLICK_FEATURES,
                GMParamTracker.Label.SEND_ANSWER
        )
    }

    fun eventPMCancellationClickBackButtonFirstPage() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                GMParamTracker.EVENT_CLICK_POWER_MERCHANT,
                GMParamTracker.Category.PM_QUESTIONNAIRE,
                GMParamTracker.Action.CLICK_RATING,
                GMParamTracker.Label.BACK_BUTTON
        )
    }

    fun eventPMCancellationClickBackButtonMiddlePage() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                GMParamTracker.EVENT_CLICK_POWER_MERCHANT,
                GMParamTracker.Category.PM_QUESTIONNAIRE,
                GMParamTracker.Action.CLICK_CANCELLATION_REASON,
                GMParamTracker.Label.BACK_BUTTON
        )
    }

    fun eventPMCancellationClickBackButtonLastPage() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                GMParamTracker.EVENT_CLICK_POWER_MERCHANT,
                GMParamTracker.Category.PM_QUESTIONNAIRE,
                GMParamTracker.Action.CLICK_FEATURES,
                GMParamTracker.Label.BACK_BUTTON
        )
    }

    fun eventPMCancellationClickNextQuestionButtonFirstPage(rating: Int) {
        val label = "$rating, 1-5"
        TrackApp.getInstance().gtm.sendGeneralEvent(
                GMParamTracker.EVENT_CLICK_POWER_MERCHANT,
                GMParamTracker.Category.PM_QUESTIONNAIRE,
                GMParamTracker.Action.CLICK_RATING,
                label
        )
    }

    fun eventPMCancellationClickNextQuestionButtonMiddlePage(questionName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                GMParamTracker.EVENT_CLICK_POWER_MERCHANT,
                GMParamTracker.Category.PM_QUESTIONNAIRE,
                GMParamTracker.Action.CLICK_CANCELLATION_REASON,
                questionName)
    }

    fun eventPMCancellationClickOptionBackButtonFirstPage() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                GMParamTracker.EVENT_CLICK_POWER_MERCHANT,
                GMParamTracker.Category.PM_QUESTIONNAIRE,
                GMParamTracker.Action.CLICK_RATING,
                GMParamTracker.Label.BACK_BUTTON
        )
    }

    fun eventPMCancellationClickOptionBackButtonMiddlePage() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                GMParamTracker.EVENT_CLICK_POWER_MERCHANT,
                GMParamTracker.Category.PM_QUESTIONNAIRE,
                GMParamTracker.Action.CLICK_CANCELLATION_REASON,
                GMParamTracker.Label.BACK_BUTTON
        )
    }

    fun eventPMCancellationClickOptionBackButtonLastPage() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                GMParamTracker.EVENT_CLICK_POWER_MERCHANT,
                GMParamTracker.Category.PM_QUESTIONNAIRE,
                GMParamTracker.Action.CLICK_FEATURES,
                GMParamTracker.Label.BACK_BUTTON
        )
    }

    fun sendScreenName(screenName: String) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName)
    }

}