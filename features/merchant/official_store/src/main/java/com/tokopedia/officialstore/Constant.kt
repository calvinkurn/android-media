package com.tokopedia.officialstore

import com.tokopedia.applink.ApplinkConst

object DynamicChannelIdentifiers {

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

    const val OFFICIAL_SEARCHBAR = "${ApplinkConst.DISCOVERY_SEARCH_AUTOCOMPLETE}?shop_tier=2&navsource=os"

    val OFFICIAL_PROMO_NATIVE = "${ApplinkConst.PROMO_LIST}?categoryID=8&menuID=363"

    const val CLICK_TYPE_WISHLIST = "&click_type=wishlist"
}

object TopAdsHeadlineConstant{
    const val PAGE = "homepage_os"
    const val SEEN_ADS = 0

}
