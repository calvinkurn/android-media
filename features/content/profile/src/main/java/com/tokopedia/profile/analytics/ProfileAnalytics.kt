package com.tokopedia.profile.analytics

/**
 * @author by milhamj on 10/10/18.
 */
class ProfileAnalytics {
    object Event {
        const val EVENT_CLICK_TOP_PROFILE = "clickTopProfile"
    }

    object Category {
        val TOP_PROFILE = "Top Profile"
        val KOL_TOP_PROFILE = "kol top profile"
    }

    object Action {
        val CLICK_ON_COMPLETE_NOW = "Click on Lengkapi Sekarang"
        val CLICK_ON_MANAGE_ACCOUNT = "Click on Atur Akun"
        val CLICK_ON_FAVORITE = "Click on Favoritkan"
        val CLICK_ON_UNFAVORITE = "Click on Unfavorite"
        val CLICK_PROMPT = "click prompt"
    }

    object Label {
        val GO_TO_FEED_FORMAT = "go to feed - %s"
    }
}