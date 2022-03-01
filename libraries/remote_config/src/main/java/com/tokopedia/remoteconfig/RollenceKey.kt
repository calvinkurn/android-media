package com.tokopedia.remoteconfig

object RollenceKey {

    // Shop Settings
    const val AB_TEST_OPERATIONAL_HOURS_KEY = "operational_hour"
    const val AB_TEST_OPERATIONAL_HOURS_NO_KEY = "no_key"

    //    Shop Page
    const val NAVIGATION_EXP_OS_BOTTOM_NAV_EXPERIMENT = "Exp_OSbotnav_Android"
    const val AB_TEST_SHOP_FOLLOW_BUTTON_KEY = "shop_follow_aug2021"
    const val AB_TEST_SHOP_FOLLOW_BUTTON_VARIANT_OLD = "follow_white_small"
    const val AB_TEST_SHOP_FOLLOW_BUTTON_VARIANT_SMALL = "follow_green_small"
    const val AB_TEST_SHOP_FOLLOW_BUTTON_VARIANT_BIG = "follow_green_big"

    const val KEY_AB_INBOX_REVAMP = "ReviewTab_NewInbox"
    const val VARIANT_OLD_INBOX = "ReviewTab_OldInbox"
    const val VARIANT_NEW_INBOX = "ReviewTab_NewInbox"

    const val BALANCE_EXP = "Balance Widget"
    const val BALANCE_VARIANT_OLD = "Existing Balance Widget"
    const val BALANCE_VARIANT_NEW = "New Balance Widget"

    const val HOME_BEAUTY_FEST = "home_cantik"

    //home component rollence section
    const val HOME_COMPONENT_LEGO24BANNER_EXP = "lego24_new"
    const val HOME_COMPONENT_LEGO24BANNER_OLD = "lego24_old"
    const val HOME_COMPONENT_LEGO24BANNER_VARIANT = "lego24_new"
    // end of home component rollence section

    const val NAVIGATION_VARIANT_OS_BOTTOM_NAV_EXPERIMENT = "control_variant"

    //TBD
    const val EXPERIMENT_NAME_TOKOPOINT = "tokopoints_glmenu"

    //Recommendation page section
    const val RECOM_PAGE_CPM_EXP = "ShopAdsIM3"
    const val RECOM_PAGE_CPM_OLD = "ShopAdsIMControl"
    const val RECOM_PAGE_CPM_VARIANT = "ShopAdsIMVariant"
    // end of Recommendation page section

    //User
    const val USER_DARK_MODE_TOGGLE = "dark_mode_nakama_v2"
    const val EXPERIMENT_NAME_REVIEW_CREDIBILITY = "review_credibility"
    const val VARIANT_REVIEW_CREDIBILITY_WITHOUT_BOTTOM_SHEET = "without_bottomsheet"
    const val VARIANT_REVIEW_CREDIBILITY_WITH_BOTTOM_SHEET = "with_bottomsheet"

    //Auto Complete
    const val AUTOCOMPLETE_INITIAL_STATE_COMPONENT_TRACKING = "keyword_track_init"
    const val AUTOCOMPLETE_SUGGESTION_COMPONENT_TRACKING = "keyword_track_autoc"

    //Search
    const val SEARCH_CAROUSEL_CONTENT_TRACKER_UNIFICATION = "ContentTrackerUni"
    const val SEARCH_BROAD_MATCH_TRACKER_UNIFICATION = "keyword_track_broad"

    // Purchase Platform
    const val WISHLIST_V2_REVAMP = "Wishlist_revamp_apps"
    const val WISHLIST_OLD_VARIANT = "control_variant"
    const val WISHLIST_V2_VARIANT = "experiment_variant"

    // advanced search negative keywords
    const val SEARCH_ADVANCED_KEYWORD_ADV_NEG = "keyword_adv_neg"
    const val SEARCH_ADVANCED_NEGATIVE_NO_ADS = "negative_no_ads"
}