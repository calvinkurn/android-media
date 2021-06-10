package com.tokopedia.officialstore

import com.tokopedia.applink.ApplinkConst

object GQLQueryConstant {
    const val QUERY_OFFICIAL_STORE_CATEGORIES = "gql_query_official_store_categories"
    const val QUERY_OFFICIAL_STORE_BANNERS = "gql_query_official_store_banners"
    const val QUERY_OFFICIAL_STORE_BENEFITS = "gql_query_official_store_benefits"
    const val QUERY_OFFICIAL_STORE_FEATURED_SHOPS = "gql_query_official_store_featured"
    const val QUERY_OFFICIAL_STORE_DYNAMIC_CHANNEL = "query_official_store_dynamic_channel"
    const val QUERY_OFFICIAL_STORE_PRODUCT_RECOMMENDATION = "gql_query_official_store_product_recommendation"
}

object DynamicChannelIdentifiers {
    val LAYOUT_UNKNOWN = "unknown"
    val LAYOUT_BANNER_CAROUSEL = "banner_carousel"
    val LAYOUT_BANNER_ = "banner_organic"
    val LAYOUT_ORGANIC = "organic"
    val LAYOUT_HOME_WIDGET = "home_widget"
    val LAYOUT_SPRINT_LEGO = "sprint_lego"
    val LAYOUT_LEGO_3_IMAGE = "lego_3_image"
    val LAYOUT_6_IMAGE = "6_image"
    val LAYOUT_MIX_LEFT = "left_carousel"
    val LAYOUT_MIX_TOP = "top_carousel"
    val LAYOUT_FEATURED_BRAND = "featured_brand"

    val CTA_MODE_MAIN = "main"
    val CTA_MODE_TRANSACTION = "transaction"
    val CTA_MODE_INVERTED = "inverted"
    val CTA_MODE_DISABLED = "disabled"
    val CTA_MODE_ALTERNATE = "alternate"

    val CTA_TYPE_FILLED = "filled"
    val CTA_TYPE_GHOST = "ghost"
    val CTA_TYPE_TEXT = "text_only"
}

object FirebasePerformanceMonitoringConstant {
    val CATEGORY = "mp_os_home_category"
    val BANNER = "mp_os_home_{slug}_banner"
    val BRAND = "mp_os_home_{slug}_featuredbrand"
    val DYNAMIC_CHANNEL = "mp_os_home_{slug}_fsdcdm"
    val PRODUCT_RECOM = "mp_os_home_{slug}_productrecom"
}

object ApplinkConstant {

    const val OFFICIAL_SEARCHBAR = "${ApplinkConst.DISCOVERY_SEARCH_AUTOCOMPLETE}?official=true&navsource=os"

    val OFFICIAL_PROMO_NATIVE = "${ApplinkConst.PROMO_LIST}?categoryID=8&menuID=363"
}
