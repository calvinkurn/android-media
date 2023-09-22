package com.tokopedia.shop.home

/**
 * TODO need to rework this class in the future by creating an enum for widget type and widget name
 * and also change widget mapping on [ShopHomeMapper] to enum
*/
object HomeConstant {
    const val YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v="
}

object WidgetType {
    const val DISPLAY = "display"
    const val PRODUCT = "product"
    const val DYNAMIC = "dynamic"
    const val CAMPAIGN = "campaign"
    const val PERSONALIZATION = "perso"
    const val SHOWCASE = "etalase"
    const val PROMO = "promo"
    const val CARD = "card"
    const val BUNDLE = "bundle"
    const val VOUCHER_SLIDER = "voucher_slider"
    const val DIRECT_PURCHASE = "direct_purchase"
    const val COMPONENT = "component"
}

/**
 * If you add a new widget name, please also add it to [WidgetNameEnum]
 */
object WidgetName {
    const val SLIDER_BANNER = "slider_banner"
    const val SLIDER_SQUARE_BANNER = "slider_square"
    const val DISPLAY_SINGLE_COLUMN = "display_single_column"
    const val DISPLAY_DOUBLE_COLUMN = "display_double_column"
    const val DISPLAY_TRIPLE_COLUMN = "display_triple_column"
    const val PLAY_CAROUSEL_WIDGET = "play"
    const val VIDEO = "video"
    const val PRODUCT = "product"
    const val PRODUCT_VERTICAL = "product_vertical"
    const val RECENT_ACTIVITY = "recent_activity"
    const val BUY_AGAIN = "buy_again"
    const val REMINDER = "reminder"
    const val ADD_ONS = "add_ons"
    const val TRENDING = "trending"
    const val NEW_PRODUCT_LAUNCH_CAMPAIGN = "promo_campaign"
    const val VOUCHER_STATIC = "voucher_static"
    const val FLASH_SALE_TOKO = "flash_sale_toko"
    const val INFO_CARD = "info_card"
    const val PRODUCT_BUNDLE_SINGLE = "single_bundling"
    const val PRODUCT_BUNDLE_MULTIPLE = "multiple_bundling"
    const val ETALASE_THEMATIC = "etalase_thematic"
    const val BIG_CAMPAIGN_THEMATIC = "big_campaign_thematic"
    const val PERSO_PRODUCT_COMPARISON = "comparison"
    const val BANNER_TIMER = "banner_timer"
    const val BANNER_PRODUCT_HOTSPOT = "banner_product_hotspot"
    const val SLIDER_BANNER_HIGHLIGHT = "slider_banner_highlight"
    const val PRODUCT_HIGHLIGHT = "product_highlight"
    const val VOUCHER = "voucher"
    const val SHOWCASE_NAVIGATION_BANNER = "showcase_navigation_banner"
    const val ADVANCED_SLIDER_BANNER = "advanced_slider_banner"
    const val BANNER_PRODUCT_GROUP = "banner_product_group"
    const val DIRECT_PURCHASED_BY_ETALASE = "direct_purchased_by_etalase"

    // showcase widget name list
    const val SHOWCASE_SLIDER_SMALL = "etalase_slider_kecil"
    const val SHOWCASE_SLIDER_MEDIUM = "etalase_slider_medium"
    const val SHOWCASE_SLIDER_TWO_ROWS = "etalase_slider_kecil_2_baris"
    const val SHOWCASE_GRID_SMALL = "etalase_banner_3_x_2"
    const val SHOWCASE_GRID_MEDIUM = "etalase_banner_besar_2x2"
    const val SHOWCASE_GRID_BIG = "etalase_banner_besar_2x1"
    const val IS_SHOW_ETALASE_NAME = 1

    val LIST_SUPPORTED_WIDGET_NAME = listOf<String>(
        SLIDER_BANNER,

    )
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
    BANNER_PRODUCT_HOTSPOT("banner_product_hotspot"),
    SLIDER_BANNER_HIGHLIGHT("slider_banner_highlight"),
    PRODUCT_HIGHLIGHT("product_highlight"),
    VOUCHER("voucher"),
    SHOWCASE_NAVIGATION_BANNER("showcase_navigation_banner"),
    ADVANCED_SLIDER_BANNER("advanced_slider_banner"),
    BANNER_PRODUCT_GROUP("banner_product_group"),
    DIRECT_PURCHASED_BY_ETALASE("direct_purchased_by_etalase"),
    SHOWCASE_SLIDER_SMALL("etalase_slider_kecil"),
    SHOWCASE_SLIDER_MEDIUM("etalase_slider_medium"),
    SHOWCASE_SLIDER_TWO_ROWS("etalase_slider_kecil_2_baris"),
    SHOWCASE_GRID_SMALL("etalase_banner_3_x_2"),
    SHOWCASE_GRID_MEDIUM("etalase_banner_besar_2x2"),
    SHOWCASE_GRID_BIG("etalase_banner_besar_2x1")
}
