package com.tokopedia.gm.common.constant

object GMParamTracker {

    const val EVENT_CLICK_POWER_MERCHANT = "clickPowerMerchant"
    const val EVENT_CLICK_HOME_PAGE = "clickHomepage"
    const val EVENT_CLICK_SHOP_SETTING = "clickShopSetting"

    const val CATEGORY_SELLER_APP = "tokopedia seller app"
    const val CATEGORY_MAIN_APP = "tokopedia main app"
    const val CATEGORY_HOME_PAGE = "tokopedia seller app - homepage"
    const val CATEGORY_PM_QUESTIONNAIRE = "power merchant questionnaire"

    const val ACTION_CLICK_POWER_MERCHANT = "click power merchant"
    const val ACTION_CLICK_SHOP_SETTINGS = "click shop settings"
    const val ACTION_CLICK_POWER_MERCHANT_PM = "click power merchant - pm"
    const val ACTION_CLICK_INCREASE_SHOP_PERFORMANCE = "click increase shop performance"

    const val LABEL_FEATURES_LEARN_MORE = "pm features - learn more"
    const val LABEL_TERMS_CONDITION = "terms condition - upgrade shop"
    const val LABEL_POP_UP_LEARN = "pop up - learn upgrade shop"
    const val LABEL_POP_UP_CANCEL_MEMBERSHIP = "pop up - cancel membership"
    const val LABEL_SHOP_INFO = "shop info - upgrade shop"
    const val LABEL_SHOP_INFO_LEARN_MORE= "shop info - learn more"

    object ScreenName {
        const val PM_SUBSCRIBE = "Power Merchant Subscribe"
    }

    object Category {
        const val PM_QUESTIONNAIRE = "power merchant questionnaire"
    }

    object Action {
        const val CLICK_FEATURES = "click features"
        const val CLICK_RATING = "click rating"
        const val CLICK_CANCELLATION_REASON = "click cancellation reason"
        const val CLICK_TNC_FREE_SHIPPING = "click t&c free ongkir"
        const val CLICK_TNC_BROADCAST_CHAT = "click t&c broadcast chat"
        const val CLICK_LEARN_MORE_UPGRADE_SHOP = "click pelajari selengkapnya upgrade tokomu"
        const val CLICK_POWER_MERCHANT = "click power merchant"
        const val CLICK_TICK_BOX_TNC = "click tickbox term & condition"
        const val CLICK_KYC_VERIFICATION = "click data verification - kyc"
        const val CLICK_DISMISS_KYC_VERIFICATION = "click close pop up - kyc"
        const val CLICK_SEE_SHOP_SCORE_TIPS = "click performance tips"
        const val CLICK_DISMISS_SHOP_SCORE = "click close pop up - score below"
        const val CLICK_START_PM_SUCCESS_NOTIFIER = "click mulai - sucess PM notifier"
        const val CLICK_CANCEL_MEMBERSHIP = "click stop subscription - power merchant page"
        const val CLICK_PERFORMANCE_TIPS = "click performance tips - inactive pm"
        const val CLICK_LEARN_MORE_PM_PAGE = "click learn more - power merchant page"
        const val CLICK_CANCEL_MEMBERSHIP_POP_UP = "click stop - pop up stop power merchant"
        const val CLICK_SUBMIT_RATING = "submit rating"
        const val CLICK_SUBMIT_CANCELLATION_REASON = "submit cancellation reason"
        const val CLICK_SUBMIT_FEATURES = "submit click features"
        const val POP_UP_DATA_VERIFICATION = "pop up data verification"
        const val POP_UP_SCORE_BELOW = "pop up - score below"
        const val POP_UP_SUCCESS_PM = "pop up - sucess PM notifier"
        const val CLICK_BACK_POP_UP_CANCEL_MEMBERSHIP = "click back - pop up stop power merchant"
    }

    object Label {
        const val BACK_BUTTON = "back button"
        const val POWER_MERCHANT = "PM"
        const val REGULAR_MERCHANT = "RM"
        const val UPGRADE_SHOP = "upgrade shop"
        const val NO_DATA = "no data"
        const val TERMS_AND_CONDITION_UPGRADE_SHOP = "terms condition - upgrade shop"
    }

    object CustomDimension {
        const val USER_ID = "userId"
        const val SHOP_ID = "shopId"
        const val SHOP_TYPE = "shopType"
    }
}