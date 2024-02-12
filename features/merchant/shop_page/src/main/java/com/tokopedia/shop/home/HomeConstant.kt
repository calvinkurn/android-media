package com.tokopedia.shop.home

object HomeConstant {
    const val YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v="
    const val IS_SHOW_ETALASE_NAME = 1
}

enum class WidgetTypeEnum(val value: String) {
    DISPLAY("display"),
    PRODUCT("product"),
    DYNAMIC("dynamic"),
    CAMPAIGN("campaign"),
    PERSONALIZATION("perso"),
    SHOWCASE("etalase"),
    PROMO("promo"),
    CARD("card"),
    BUNDLE("bundle"),
    VOUCHER_SLIDER("voucher_slider"),
    REIMAGINE_DIRECT_PURCHASE("direct_purchase"),
    REIMAGINE_COMPONENT("component"),
    GROUP_OFFERING_PRODUCT("group_offering_product")
}

enum class WidgetNameEnum(val value: String) {
    SLIDER_BANNER("slider_banner"),
    SLIDER_SQUARE_BANNER("slider_square"),
    DISPLAY_SINGLE_COLUMN("display_single_column"),
    DISPLAY_DOUBLE_COLUMN("display_double_column"),
    DISPLAY_TRIPLE_COLUMN("display_triple_column"),
    PLAY_CAROUSEL_WIDGET("play"),
    VIDEO("video"),
    PRODUCT("product"),
    PRODUCT_VERTICAL("product_vertical"),
    RECENT_ACTIVITY("recent_activity"),
    BUY_AGAIN("buy_again"),
    REMINDER("reminder"),
    ADD_ONS("add_ons"),
    TRENDING("trending"),
    NEW_PRODUCT_LAUNCH_CAMPAIGN("promo_campaign"),
    VOUCHER_STATIC("voucher_static"),
    FLASH_SALE_TOKO("flash_sale_toko"),
    INFO_CARD("info_card"),
    PRODUCT_BUNDLE_SINGLE("single_bundling"),
    PRODUCT_BUNDLE_MULTIPLE("multiple_bundling"),
    ETALASE_THEMATIC("etalase_thematic"),
    BIG_CAMPAIGN_THEMATIC("big_campaign_thematic"),
    PERSO_PRODUCT_COMPARISON("comparison"),
    BANNER_TIMER("banner_timer"),
    REIMAGINE_BANNER_PRODUCT_HOTSPOT("banner_product_hotspot"),
    SLIDER_BANNER_HIGHLIGHT("slider_banner_highlight"),
    PRODUCT_HIGHLIGHT("product_highlight"),
    VOUCHER("voucher"), // Buy More Get More Widget Banner
    SHOWCASE_NAVIGATION_BANNER("showcase_navigation_banner"),
    ADVANCED_SLIDER_BANNER("advanced_slider_banner"),
    BANNER_PRODUCT_GROUP("banner_product_group"),
    DIRECT_PURCHASED_BY_ETALASE("direct_purchased_by_etalase"),
    SHOWCASE_SLIDER_SMALL("etalase_slider_kecil"),
    SHOWCASE_SLIDER_MEDIUM("etalase_slider_medium"),
    SHOWCASE_SLIDER_TWO_ROWS("etalase_slider_kecil_2_baris"),
    SHOWCASE_GRID_SMALL("etalase_banner_3_x_2"),
    SHOWCASE_GRID_MEDIUM("etalase_banner_besar_2x2"),
    SHOWCASE_GRID_BIG("etalase_banner_besar_2x1"),
    BMSM_GWP_OFFERING_GROUP("gwp_offering_group"),
    BMSM_PD_OFFERING_GROUP("bmgm_banner_group")
}
