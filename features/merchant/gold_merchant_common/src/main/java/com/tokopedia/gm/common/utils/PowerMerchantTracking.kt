package com.tokopedia.gm.common.utils

import com.tokopedia.config.GlobalConfig
import com.tokopedia.gm.common.constant.GMParamTracker
import com.tokopedia.gm.common.constant.GMParamTracker.CustomDimension
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject


class PowerMerchantTracking @Inject constructor(
    private val user: UserSessionInterface
) {

    fun eventLearnMorePm() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                GMParamTracker.EVENT_CLICK_POWER_MERCHANT,
                GMParamTracker.CATEGORY_SELLER_APP,
                GMParamTracker.ACTION_CLICK_POWER_MERCHANT,
                GMParamTracker.LABEL_FEATURES_LEARN_MORE)
    }

    fun eventUpgradeShopWebView() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                GMParamTracker.EVENT_CLICK_POWER_MERCHANT,
                GMParamTracker.CATEGORY_SELLER_APP,
                GMParamTracker.ACTION_CLICK_POWER_MERCHANT,
                GMParamTracker.LABEL_TERMS_CONDITION)
    }

    fun eventLearnMoreSuccessPopUp() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                GMParamTracker.EVENT_CLICK_POWER_MERCHANT,
                GMParamTracker.CATEGORY_SELLER_APP,
                GMParamTracker.ACTION_CLICK_POWER_MERCHANT,
                GMParamTracker.LABEL_POP_UP_LEARN)
    }

    fun eventUpgradeShopSetting() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                GMParamTracker.EVENT_CLICK_SHOP_SETTING,
                GMParamTracker.CATEGORY_SELLER_APP,
                GMParamTracker.ACTION_CLICK_SHOP_SETTINGS,
                GMParamTracker.LABEL_SHOP_INFO)
    }

    fun eventLearnMoreSetting() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                GMParamTracker.EVENT_CLICK_SHOP_SETTING,
                GMParamTracker.CATEGORY_SELLER_APP,
                GMParamTracker.ACTION_CLICK_SHOP_SETTINGS,
                GMParamTracker.LABEL_SHOP_INFO_LEARN_MORE)
    }

    fun eventCancelMembershipBottomSheet() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                GMParamTracker.EVENT_CLICK_POWER_MERCHANT,
                GMParamTracker.CATEGORY_SELLER_APP,
                GMParamTracker.ACTION_CLICK_POWER_MERCHANT_PM,
                GMParamTracker.LABEL_POP_UP_CANCEL_MEMBERSHIP)
    }

    fun eventIncreaseScoreBottomSheet() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                GMParamTracker.EVENT_CLICK_HOME_PAGE,
                GMParamTracker.CATEGORY_HOME_PAGE,
                GMParamTracker.ACTION_CLICK_INCREASE_SHOP_PERFORMANCE,
                "")
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

    fun eventClickFreeShippingTnC() {
        val category = getCategory()

        val event = TrackAppUtils.gtmData(
            GMParamTracker.EVENT_CLICK_POWER_MERCHANT,
            category,
            GMParamTracker.Action.CLICK_TNC_FREE_SHIPPING,
            ""
        )

        event[CustomDimension.USER_ID] = user.userId
        event[CustomDimension.SHOP_ID] = user.shopId
        event[CustomDimension.SHOP_TYPE] = getShopType()

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun eventClickBroadcastTnC() {
        val category = getCategory()

        val event = TrackAppUtils.gtmData(
            GMParamTracker.EVENT_CLICK_POWER_MERCHANT,
            category,
            GMParamTracker.Action.CLICK_TNC_BROADCAST_CHAT,
            ""
        )

        event[CustomDimension.USER_ID] = user.userId
        event[CustomDimension.SHOP_ID] = user.shopId
        event[CustomDimension.SHOP_TYPE] = getShopType()

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun eventClickLearnMoreUpgradeShop() {
        val category = getCategory()

        val event = TrackAppUtils.gtmData(
            GMParamTracker.EVENT_CLICK_POWER_MERCHANT,
            category,
            GMParamTracker.Action.CLICK_LEARN_MORE_UPGRADE_SHOP,
            ""
        )

        event[CustomDimension.USER_ID] = user.userId
        event[CustomDimension.SHOP_ID] = user.shopId
        event[CustomDimension.SHOP_TYPE] = getShopType()

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun eventClickUpgradeShop() {
        val category = getCategory()

        val event = TrackAppUtils.gtmData(
            GMParamTracker.EVENT_CLICK_POWER_MERCHANT,
            category,
            GMParamTracker.Action.CLICK_POWER_MERCHANT,
            GMParamTracker.Label.UPGRADE_SHOP
        )

        event[CustomDimension.USER_ID] = user.userId
        event[CustomDimension.SHOP_ID] = user.shopId
        event[CustomDimension.SHOP_TYPE] = getShopType()

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun eventClickTermsAndConditionTickBox() {
        val category = getCategory()

        val event = TrackAppUtils.gtmData(
            GMParamTracker.EVENT_CLICK_POWER_MERCHANT,
            category,
            GMParamTracker.Action.CLICK_TICK_BOX_TNC,
            GMParamTracker.Label.NO_DATA
        )

        event[CustomDimension.USER_ID] = user.userId
        event[CustomDimension.SHOP_ID] = user.shopId
        event[CustomDimension.SHOP_TYPE] = getShopType()

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun eventClickTermsAndConditionUpgradeBtn() {
        val category = getCategory()

        val event = TrackAppUtils.gtmData(
            GMParamTracker.EVENT_CLICK_POWER_MERCHANT,
            category,
            GMParamTracker.Action.CLICK_POWER_MERCHANT,
            GMParamTracker.Label.TERMS_AND_CONDITION_UPGRADE_SHOP
        )

        event[CustomDimension.USER_ID] = user.userId
        event[CustomDimension.SHOP_ID] = user.shopId
        event[CustomDimension.SHOP_TYPE] = getShopType()

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun eventClickKycVerification() {
        val category = getCategory()

        val event = TrackAppUtils.gtmData(
            GMParamTracker.EVENT_CLICK_POWER_MERCHANT,
            category,
            GMParamTracker.Action.CLICK_KYC_VERIFICATION,
            ""
        )

        event[CustomDimension.USER_ID] = user.userId
        event[CustomDimension.SHOP_ID] = user.shopId
        event[CustomDimension.SHOP_TYPE] = getShopType()

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun eventClickDismissKycPopUp() {
        val category = getCategory()

        val event = TrackAppUtils.gtmData(
            GMParamTracker.EVENT_CLICK_POWER_MERCHANT,
            category,
            GMParamTracker.Action.CLICK_DISMISS_KYC_VERIFICATION,
            ""
        )

        event[CustomDimension.USER_ID] = user.userId
        event[CustomDimension.SHOP_ID] = user.shopId
        event[CustomDimension.SHOP_TYPE] = getShopType()

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun eventClickSeeShopScoreTips(shopScore: Int) {
        val category = getCategory()

        val event = TrackAppUtils.gtmData(
            GMParamTracker.EVENT_CLICK_POWER_MERCHANT,
            category,
            GMParamTracker.Action.CLICK_SEE_SHOP_SCORE_TIPS,
            shopScore.toString()
        )

        event[CustomDimension.USER_ID] = user.userId
        event[CustomDimension.SHOP_ID] = user.shopId
        event[CustomDimension.SHOP_TYPE] = getShopType()

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun eventClickDismissShopScorePopUp(shopScore: Int) {
        val category = getCategory()

        val event = TrackAppUtils.gtmData(
            GMParamTracker.EVENT_CLICK_POWER_MERCHANT,
            category,
            GMParamTracker.Action.CLICK_DISMISS_SHOP_SCORE,
            shopScore.toString()
        )

        event[CustomDimension.USER_ID] = user.userId
        event[CustomDimension.SHOP_ID] = user.shopId
        event[CustomDimension.SHOP_TYPE] = getShopType()

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun eventClickStartSuccessBottomSheet() {
        val category = getCategory()

        val event = TrackAppUtils.gtmData(
            GMParamTracker.EVENT_CLICK_POWER_MERCHANT,
            category,
            GMParamTracker.Action.CLICK_START_PM_SUCCESS_NOTIFIER,
            ""
        )

        event[CustomDimension.USER_ID] = user.userId
        event[CustomDimension.SHOP_ID] = user.shopId
        event[CustomDimension.SHOP_TYPE] = getShopType()

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun eventClickCancelMembership() {
        val category = getCategory()

        val event = TrackAppUtils.gtmData(
            GMParamTracker.EVENT_CLICK_POWER_MERCHANT,
            category,
            GMParamTracker.Action.CLICK_CANCEL_MEMBERSHIP,
            ""
        )

        event[CustomDimension.USER_ID] = user.userId
        event[CustomDimension.SHOP_ID] = user.shopId
        event[CustomDimension.SHOP_TYPE] = getShopType()

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun eventClickCancelMembershipPopUp() {
        val category = getCategory()

        val event = TrackAppUtils.gtmData(
            GMParamTracker.EVENT_CLICK_POWER_MERCHANT,
            category,
            GMParamTracker.Action.CLICK_CANCEL_MEMBERSHIP_POP_UP,
            ""
        )

        event[CustomDimension.USER_ID] = user.userId
        event[CustomDimension.SHOP_ID] = user.shopId
        event[CustomDimension.SHOP_TYPE] = getShopType()

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun eventClickPerformanceTipsBtn() {
        val category = getCategory()

        val event = TrackAppUtils.gtmData(
            GMParamTracker.EVENT_CLICK_POWER_MERCHANT,
            category,
            GMParamTracker.Action.CLICK_PERFORMANCE_TIPS,
            ""
        )

        event[CustomDimension.USER_ID] = user.userId
        event[CustomDimension.SHOP_ID] = user.shopId
        event[CustomDimension.SHOP_TYPE] = getShopType()

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun eventLearMoreFreeShipping() {
        val category = getCategory()

        val event = TrackAppUtils.gtmData(
            GMParamTracker.EVENT_CLICK_POWER_MERCHANT,
            category,
            GMParamTracker.Action.CLICK_LEARN_MORE_PM_PAGE,
            ""
        )

        event[CustomDimension.USER_ID] = user.userId
        event[CustomDimension.SHOP_ID] = user.shopId
        event[CustomDimension.SHOP_TYPE] = getShopType()

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun eventClickCancellationQuestionnaireRating(rating: Int) {
        val event = TrackAppUtils.gtmData(
            GMParamTracker.EVENT_CLICK_POWER_MERCHANT,
            GMParamTracker.CATEGORY_PM_QUESTIONNAIRE,
            GMParamTracker.Action.CLICK_RATING,
            rating.toString()
        )

        event[CustomDimension.USER_ID] = user.userId
        event[CustomDimension.SHOP_ID] = user.shopId
        event[CustomDimension.SHOP_TYPE] = getShopType()

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun eventClickCancellationQuestionnaireSubmitRating(rating: Int) {
        val event = TrackAppUtils.gtmData(
            GMParamTracker.EVENT_CLICK_POWER_MERCHANT,
            GMParamTracker.CATEGORY_PM_QUESTIONNAIRE,
            GMParamTracker.Action.CLICK_SUBMIT_RATING,
            rating.toString()
        )

        event[CustomDimension.USER_ID] = user.userId
        event[CustomDimension.SHOP_ID] = user.shopId
        event[CustomDimension.SHOP_TYPE] = getShopType()

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun eventClickCancellationReasonQuestionnaire(option: String) {
        val event = TrackAppUtils.gtmData(
            GMParamTracker.EVENT_CLICK_POWER_MERCHANT,
            GMParamTracker.CATEGORY_PM_QUESTIONNAIRE,
            GMParamTracker.Action.CLICK_CANCELLATION_REASON,
            option
        )

        event[CustomDimension.USER_ID] = user.userId
        event[CustomDimension.SHOP_ID] = user.shopId
        event[CustomDimension.SHOP_TYPE] = getShopType()

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun eventClickCancellationQuestionnaireSubmitReason(options: List<String>) {
        val event = TrackAppUtils.gtmData(
            GMParamTracker.EVENT_CLICK_POWER_MERCHANT,
            GMParamTracker.CATEGORY_PM_QUESTIONNAIRE,
            GMParamTracker.Action.CLICK_SUBMIT_CANCELLATION_REASON,
            options.toString()
        )

        event[CustomDimension.USER_ID] = user.userId
        event[CustomDimension.SHOP_ID] = user.shopId
        event[CustomDimension.SHOP_TYPE] = getShopType()

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun eventClickCancellationFeatureQuestionnaire(option: String) {
        val event = TrackAppUtils.gtmData(
            GMParamTracker.EVENT_CLICK_POWER_MERCHANT,
            GMParamTracker.CATEGORY_PM_QUESTIONNAIRE,
            GMParamTracker.Action.CLICK_FEATURES,
            option
        )

        event[CustomDimension.USER_ID] = user.userId
        event[CustomDimension.SHOP_ID] = user.shopId
        event[CustomDimension.SHOP_TYPE] = getShopType()

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun eventClickCancellationQuestionnaireSendAnswer(options: List<String>) {
        val event = TrackAppUtils.gtmData(
            GMParamTracker.EVENT_CLICK_POWER_MERCHANT,
            GMParamTracker.CATEGORY_PM_QUESTIONNAIRE,
            GMParamTracker.Action.CLICK_SUBMIT_FEATURES,
            options.toString()
        )

        event[CustomDimension.USER_ID] = user.userId
        event[CustomDimension.SHOP_ID] = user.shopId
        event[CustomDimension.SHOP_TYPE] = getShopType()

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun eventShowDialogKyc() {
        val category = getCategory()

        val event = TrackAppUtils.gtmData(
            GMParamTracker.EVENT_CLICK_POWER_MERCHANT,
            category,
            GMParamTracker.Action.POP_UP_DATA_VERIFICATION,
            ""
        )

        event[CustomDimension.USER_ID] = user.userId
        event[CustomDimension.SHOP_ID] = user.shopId
        event[CustomDimension.SHOP_TYPE] = getShopType()

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun eventShowDialogScore(shopScore: Int) {
        val category = getCategory()

        val event = TrackAppUtils.gtmData(
            GMParamTracker.EVENT_CLICK_POWER_MERCHANT,
            category,
            GMParamTracker.Action.POP_UP_SCORE_BELOW,
            shopScore.toString()
        )

        event[CustomDimension.USER_ID] = user.userId
        event[CustomDimension.SHOP_ID] = user.shopId
        event[CustomDimension.SHOP_TYPE] = getShopType()

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun eventPowerMerchantSuccessBottomSheet() {
        val category = getCategory()

        val event = TrackAppUtils.gtmData(
            GMParamTracker.EVENT_CLICK_POWER_MERCHANT,
            category,
            GMParamTracker.Action.POP_UP_SUCCESS_PM,
            ""
        )

        event[CustomDimension.USER_ID] = user.userId
        event[CustomDimension.SHOP_ID] = user.shopId
        event[CustomDimension.SHOP_TYPE] = getShopType()

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun eventClickBackCancelMembership() {
        val category = getCategory()

        val event = TrackAppUtils.gtmData(
            GMParamTracker.EVENT_CLICK_POWER_MERCHANT,
            category,
            GMParamTracker.Action.CLICK_BACK_POP_UP_CANCEL_MEMBERSHIP,
            ""
        )

        event[CustomDimension.USER_ID] = user.userId
        event[CustomDimension.SHOP_ID] = user.shopId
        event[CustomDimension.SHOP_TYPE] = getShopType()

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun eventOpenScreen(screeName: String) {
        val event = mapOf(
            CustomDimension.SHOP_TYPE to getShopType()
        )
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screeName, event)
    }

    private fun getShopType(): String {
        val isPowerMerchant = user.isGoldMerchant

        return if (isPowerMerchant) {
            GMParamTracker.Label.POWER_MERCHANT
        } else {
            GMParamTracker.Label.REGULAR_MERCHANT
        }
    }

    private fun getCategory(): String {
        return if (GlobalConfig.isSellerApp()) {
            GMParamTracker.CATEGORY_SELLER_APP
        } else {
            GMParamTracker.CATEGORY_MAIN_APP
        }
    }
}