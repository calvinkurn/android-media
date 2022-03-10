package com.tokopedia.home_account.explicitprofile.trackers

object ExplicitProfileAnalyticConstants {

    const val EVENT_BUSINESS_UNIT = "businessUnit"
    const val EVENT_CURRENT_SITE = "currentSite"

    const val BUSINESS_UNIT_USER_PLATFORM = "User Platform"
    const val TOKOPEDIA_MARKETPLACE_SITE = "tokopediamarketplace"

    object Event {
        const val CLICK_EXPLICIT_PROFILE = "clickExplicitProfile"
    }

    object Category {
        const val EXPLICIT_PROFILE_FORM_PAGE = "explicit form page"
    }

    object Action {
        const val CLICK_RESET_PREFERENCE = "click reset preferensi"
        const val CLICK_CATEGORY_TABS = "click category tabs"
        const val CLICK_INFORMATION_ICON = "click information icon"
        const val CLICK_OPTION_VALUE = "click option value"
        const val CLICK_SAVE = "click simpan"
    }

    object Label {
        const val SUCCESS = "success"
        const val FAIL = "fail"
    }
}