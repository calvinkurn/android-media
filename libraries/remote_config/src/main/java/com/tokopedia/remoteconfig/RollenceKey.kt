package com.tokopedia.remoteconfig

object RollenceKey {

    //    Shop Page
    const val AB_TEST_SHOP_AFFILIATE_SHARE_ICON = "shareIconShopAff"
    const val AB_TEST_SHOP_RE_IMAGINED = "shop_page_reimagined"

    const val AB_TEST_GRADUAL_ROLLOUT_KEY_SHOP_INFO_REIMAGINED = "an_shop_info_revamp"

    // home rollence section
    const val BALANCE_EXP = "Balance Widget"
    const val BALANCE_VARIANT_OLD = "Existing Balance Widget"
    const val BALANCE_VARIANT_NEW = "New Balance Widget"
    const val HOME_LOAD_TIME_KEY = "load_time"
    const val HOME_LOAD_TIME_CONTROL = "control_variant"
    const val HOME_LOAD_ATF_CACHE_ROLLENCE_KEY = "loadtime_homepage_an"
    const val HOME_LOAD_ATF_CACHE_ROLLENCE_CONTROL = "control_variant"
    const val HOME_LOAD_ATF_CACHE_ROLLENCE_EXP = "experiment_variant"
    const val HOME_MEGATAB = "megatab_ff"
    const val HOME_MISSION_SIZE_KEY = "mission_px"
    const val HOME_MISSION_SIZE_CONTROL = "control_var"
    const val HOME_MISSION_SIZE_VARIANT = "exp_var"
    const val HOME_GLOBAL_COMPONENT_FALLBACK = "android_fy_infinite_global_recom"
    // end of home rollence section

    // TBD
    const val EXPERIMENT_NAME_TOKOPOINT = "tokopoints_glmenu"

    // Recommendation page section
    const val RECOM_PAGE_CPM_EXP = "ShopAdsIM3"
    const val RECOM_PAGE_CPM_OLD = "ShopAdsIMControl"
    const val RECOM_PAGE_CPM_VARIANT = "ShopAdsIMVariant"
    // end of Recommendation page section

    // Icon Jumper For You
    const val ICON_JUMPER_DEFAULT = "control_variant"
    const val ICON_JUMPER_EXP = "VariantA"
    const val ICON_JUMPER = "IconJumper"

    // Auto Complete
    const val SEARCH_BROAD_MATCH_TRACKER_UNIFICATION = "keyword_track_broad"

    // Purchase Platform
    const val CONTROL_VARIANT = "control_variant"
    const val EXPERIMENT_VARIANT = "experiment_variant"
    const val TREATMENT_VARIANT = "treatment_variant"
    const val CART_BUY_AGAIN = "buyagain_cart"
    const val CART_BUY_AGAIN_CONTROL = "no_buyagain_cart"
    const val CART_BUY_AGAIN_VARIANT = "with_buyagain_cart"
    const val PROMO_ENTRY_POINT_IMPROVEMENT = "promowidget_cartco"
    const val PROMO_ENTRY_POINT_NEW = "new_promowidget"

    // SRP Video Sneak Peek Autoplay
    const val SEARCH_VIDEO_SNEAK_PEEK_AUTOPLAY = "srp_prodvideo_3"
    const val SEARCH_VIDEO_SNEAK_PEEK_AUTOPLAY_VARIANT = "experiment_variant"
    const val SEARCH_VIDEO_SNEAK_PEEK_AUTOPLAY_OTHER_VARIANT = "exp_variant"

    // SRP Similar Search Coachmark
    const val SEARCH_SIMILAR_SEARCH_COACHMARK = "coachmark_similar"
    const val SEARCH_SIMILAR_SEARCH_COACHMARK_VARIANT = "experiment_variant"

    // Product Card Experiment
    const val PRODUCT_CARD_EXPERIMENT = "newcard2"
    const val PRODUCT_CARD_EXPERIMENT_GIMMICK = "gimmick"
    const val PRODUCT_CARD_EXPERIMENT_PORTRAIT = "portrait"
    const val PRODUCT_CARD_EXPERIMENT_ETA = "eta"

    // For You Rollence
    const val FOR_YOU_FEATURE_FLAG = "foryou_feature_flag"

    // Explore Category Experiment
    const val EXPLORE_CATEGORY_DEFAULT = "control_variant"
    const val EXPLORE_CATEGORY_EXP = "GetJelajah"
    const val JELAJAH_REVAMP = "JelajahRevamp"

    // PDP
    const val PDP_PREFETCH = "prefetch_pdp_android"
    const val PDP_PREFETCH_ENABLE = "variant_prefetch"
    const val PDP_PREFETCH_DISABLE = "control_prefetch"

    // Review
    const val CREATE_REVIEW_REVIEW_INSPIRATION_EXPERIMENT_NAME = "review_inspiration"

    // Fulfillment
    const val KEY_DISABLE_DILAYANI_TOKOPEDIA_HOMEPAGE = "disabledthomepage"

    // DG SBM Transition
    const val KEY_SBM_TRANSITION = "sbm_ticker_mybills"

    // BCA Rollence
    const val BCA_ROLLENCE = "android_flazzrollout"

    // Payment
    const val THANKYOU_PAGE_WIDGET_ORDERING = "android_thankyou_v0"
    const val THANKYOU_PAGE_WIDGET_VARIANT = "TYP_Exp_PG_DG"

    // TokopediaNOW
    const val TOKOPEDIA_NOW_PAGINATION = "now_pagination"
    const val TOKOPEDIA_NOW_EXPERIMENT = "now_experiment"

    // FS Toko OOS Gradual Rollout
    const val FLASH_SALE_OUT_OF_STOCK_GRADUAL_ROLLOUT = "fst_oosimprovement"

    // Seller Order Management
    const val KEY_SOM_OG = "android_som_og"

    // Share affiliate
    const val AFFILIATE_SHARE_ICON = "ShareAff_pdpshop"

    // Wishlist
    const val WISHLIST_AFFILIATE_TICKER = "aff_ticker_col"

    // Search Reimagine
    const val SEARCH_1_INST_AUTO = "inst_auto_v2"
    const val SEARCH_1_INST_AUTO_CONTROL = "control_variant"
    const val SEARCH_1_INST_AUTO_VARIANT_1 = "variant_1_is"
    const val SEARCH_1_INST_AUTO_VARIANT_2 = "variant_2_is"
    const val SEARCH_1_INST_AUTO_VARIANT_3 = "variant_1_ac"
    const val SEARCH_1_INST_AUTO_VARIANT_4 = "variant_2_ac"
    const val PRODUCT_CARD_SRE_2024 = "productcard_sre2024"
    const val SEARCH_ROLLOUT_UNIVERSE_V2 = "rollout_universe_v2"
    const val REVERSE_PRODUCT_CARD = "reverse_productcard"
    const val REVERSE_PRODUCT_CARD_CONTROL = "control_variant"
    const val REVERSE_PRODUCT_CARD_V4 = "v4_productcard"

    const val ANDROID_INTERNAL_TEST = "android_internal"

    // Travel
    const val FLIGHT_INSURANCE_AUTO_TICK = "Flight_Ins_Att"
    const val FLIGHT_INSURANCE_AUTO_TICK_CONTROL = "flight_ins_with_att"
    const val FLIGHT_INSURANCE_AUTO_TICK_VARIANT = "flight_ins_wout_att"
}
