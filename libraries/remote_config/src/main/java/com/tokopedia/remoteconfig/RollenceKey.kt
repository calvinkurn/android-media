package com.tokopedia.remoteconfig

object RollenceKey {

    //    Shop Page
    const val NAVIGATION_EXP_OS_BOTTOM_NAV_EXPERIMENT = "Exp_OSbotnav_Android"
    const val AB_TEST_SHOP_FOLLOW_BUTTON_KEY = "shop_follow_aug2021"
    const val AB_TEST_SHOP_FOLLOW_BUTTON_VARIANT_OLD = "follow_white_small"
    const val AB_TEST_SHOP_FOLLOW_BUTTON_VARIANT_SMALL = "follow_green_small"
    const val AB_TEST_SHOP_FOLLOW_BUTTON_VARIANT_BIG = "follow_green_big"

    const val BALANCE_EXP = "Balance Widget"
    const val BALANCE_VARIANT_OLD = "Existing Balance Widget"
    const val BALANCE_VARIANT_NEW = "New Balance Widget"

    // home component rollence section
    const val HOME_COMPONENT_LEGO24BANNER_EXP = "lego24_new"
    const val HOME_COMPONENT_LEGO24BANNER_OLD = "lego24_old"
    const val HOME_COMPONENT_LEGO24BANNER_VARIANT = "lego24_new"
    const val HOME_COMPONENT_HPB_DURATION_EXP = "hpb_scroll_duration"
    const val HOME_COMPONENT_HPB_DURATION_CONTROL = "control_scroll_5s"
    const val HOME_COMPONENT_HPB_DURATION_VARIANT_4S = "scroll_4s"
    const val HOME_COMPONENT_HPB_DURATION_VARIANT_6S = "scroll_6s"
    const val HOME_COMPONENT_HPB_DOTS_INFINITE_EXP = "hpb_dots_infinite_2"
    const val HOME_COMPONENT_HPB_DOTS_INFINITE_CONTROL = "control_variant"
    const val HOME_COMPONENT_HPB_DOTS_INFINITE_VARIANT = "new_dots_scroll"
    const val HOME_COMPONENT_DYNAMIC_ICON_EXP = "dynamic_icon_new"
    const val HOME_COMPONENT_DYNAMIC_ICON_VARIANT = "dynamic_icon_new"
    // end of home component rollence section

    const val NAVIGATION_VARIANT_OS_BOTTOM_NAV_EXPERIMENT = "control_variant"

    // TBD
    const val EXPERIMENT_NAME_TOKOPOINT = "tokopoints_glmenu"

    // Recommendation page section
    const val RECOM_PAGE_CPM_EXP = "ShopAdsIM3"
    const val RECOM_PAGE_CPM_OLD = "ShopAdsIMControl"
    const val RECOM_PAGE_CPM_VARIANT = "ShopAdsIMVariant"
    // end of Recommendation page section

    // Auto Complete
    const val AUTOCOMPLETE_INITIAL_STATE_COMPONENT_TRACKING = "keyword_track_init"
    const val AUTOCOMPLETE_SUGGESTION_COMPONENT_TRACKING = "keyword_track_autoc"

    const val SEARCH_BROAD_MATCH_TRACKER_UNIFICATION = "keyword_track_broad"

    // Purchase Platform
    const val CONTROL_VARIANT = "control_variant"
    const val EXPERIMENT_VARIANT = "experiment_variant"
    const val WISHLIST_COLLECTION = "WL_collection_andro"
    const val WISHLIST_COLLECTION_SHARING = "WL_sharing_andro"
    const val UOH_REPURCHASE = "uoh_buyagainexp_andr"

    // SRP Video Sneak Peek Autoplay
    const val SEARCH_VIDEO_SNEAK_PEEK_AUTOPLAY = "srp_prodvideo_3"
    const val SEARCH_VIDEO_SNEAK_PEEK_AUTOPLAY_VARIANT = "experiment_variant"

    // Typo Correction
    const val SEARCH_TYPO_CORRECTION_ADS = "typo_correction_ads2"
    const val SEARCH_TYPO_CORRECTION_ADS_VARIANT = "typo2"

    // Global Menu (Me Page)
    const val ME_PAGE_REVAMP = "Navigation_Mepage"
    const val ME_PAGE_REVAMP_VARIANT = "Mepage_Variant"

    // TokoChat
    const val KEY_ROLLENCE_TOKOCHAT = "gofood_chat"

    // PDP
    const val PDP_CAROUSEL_ANDROID = "pdp_carousel_android"
    const val PDP_SHOW_THUMBNAIL = "pdp_show_thumbnail"
    const val PDP_HIDE_THUMBNAIL = "pdp_hide_thumbnail"
    const val PDP_SHOW_SHARE_AFFILIATE = "pdp_share_icon"

    // Saldo
    const val SALDO_MODAL_TOKO_WIDGET = "saldo_mclremoval"

    // Review
    const val CREATE_REVIEW_REVIEW_INSPIRATION_EXPERIMENT_NAME = "review_inspiration"

    // Play
    const val SWIPE_LIVE_ROOM = "play_sc"

    // Bubbles
    const val KEY_ROLLENCE_BUBBLE_CHAT = "seller_bubble_chat_2"

    // Logistic
    const val KEY_SHARE_ADDRESS_LOGI = "share_address_logi"

    object PdpToolbar {
        const val key = "pdp_dyn_container"
        const val transparent = "var_container"
    }
}
