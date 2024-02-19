package com.tokopedia.remoteconfig

object RollenceKey {

    //    Shop Page
    const val AB_TEST_SHOP_FOLLOW_BUTTON_KEY = "shop_follow_aug2021"
    const val AB_TEST_SHOP_FOLLOW_BUTTON_VARIANT_OLD = "follow_white_small"
    const val AB_TEST_SHOP_FOLLOW_BUTTON_VARIANT_SMALL = "follow_green_small"
    const val AB_TEST_SHOP_FOLLOW_BUTTON_VARIANT_BIG = "follow_green_big"
    const val AB_TEST_SHOP_AFFILIATE_SHARE_ICON = "shareIconShopAff"
    const val AB_TEST_SHOP_RE_IMAGINED = "shop_page_reimagined"

    const val AB_TEST_GRADUAL_ROLLOUT_KEY_SHOP_INFO_REIMAGINED = "an_shop_info_revamp"

    // home rollence section
    const val BALANCE_EXP = "Balance Widget"
    const val BALANCE_VARIANT_OLD = "Existing Balance Widget"
    const val BALANCE_VARIANT_NEW = "New Balance Widget"
    const val HOME_COMPONENT_ATF = "test_atf"
    const val HOME_COMPONENT_ATF_2 = "control_atf2"
    const val HOME_COMPONENT_ATF_3 = "exp_atf3"
    const val HOME_LOAD_TIME_KEY = "load_time"
    const val HOME_LOAD_TIME_CONTROL = "control_variant"
    const val HOME_LOAD_ATF_CACHE_ROLLENCE_KEY = "loadtime_homepage_an"
    const val HOME_LOAD_ATF_CACHE_ROLLENCE_CONTROL = "control_variant"
    const val HOME_LOAD_ATF_CACHE_ROLLENCE_EXP = "experiment_variant"
    const val HOME_MEGATAB = "megatab_ff"
    const val HOME_MISSION_SIZE_KEY = "mission_px"
    const val HOME_MISSION_SIZE_CONTROL = "control_var"
    const val HOME_MISSION_SIZE_VARIANT = "exp_var"
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
    const val ICON_JUMPER_SRE_KEY = "SREbottomnav"
    const val ICON_JUMPER_SRE_VALUE = "GetIcon"

    // Auto Complete
    const val SEARCH_BROAD_MATCH_TRACKER_UNIFICATION = "keyword_track_broad"

    // Purchase Platform
    const val CONTROL_VARIANT = "control_variant"
    const val EXPERIMENT_VARIANT = "experiment_variant"
    const val TREATMENT_VARIANT = "treatment_variant"
    const val UOH_REPURCHASE = "uoh_buyagainexp_andr"
    const val CART_CHECKOUT_REVAMP = "cartcheckout_revamp"
    const val CART_CHECKOUT_NEW = "cart_checkout_new"
    const val PROMO_ENTRY_POINT_IMPROVEMENT = "promowidget_cartco"
    const val PROMO_ENTRY_POINT_NEW = "new_promowidget"
    const val UOH_BUY_AGAIN_WIDGET = "buyagain_uoh"
    const val UOH_BUY_AGAIN_WIDGET_CONTROL = "without_buyagain_uoh"
    const val UOH_BUY_AGAIN_WIDGET_VARIANT = "with_buyagain_uoh"

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
    const val TOKOPEDIA_NOW_AFFILIATE = "aff_now_att"
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
    const val ANDROID_INTERNAL_TEST = "android_internal"
}
