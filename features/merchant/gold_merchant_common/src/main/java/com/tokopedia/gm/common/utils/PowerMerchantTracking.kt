package com.tokopedia.gm.common.utils

import com.tokopedia.gm.common.constant.GMParamTracker
import com.tokopedia.track.TrackApp

object PowerMerchantTracking {

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
}