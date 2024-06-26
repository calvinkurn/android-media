package com.tokopedia.common

import com.tokopedia.applink.ApplinkConst

object ProductServiceWidgetConstant {
    const val SQUAD_VALUE = "android_minion_ken"
    const val USECASE_BUNDLE_VALUE = "product_bundle_page"
    const val USECASE_GIFTING_VALUE = "product_gifting_page"
    const val SQUAD_VALUE_ADDON = "android"
    const val SQUAD_VALUE_ADDON_PDP = "pdp_platform_post_atc"
    const val USECASE_ADDON_VALUE = "product_addon_component"
    const val PRODUCT_BUNDLE_APPLINK_WITH_PARAM = ApplinkConst.PRODUCT_BUNDLE + "?bundleId={bundle_id}&source={page_source}"
    const val PRODUCT_ID_DEFAULT_VALUE = "0"
    const val PRODUCT_BUNDLE_REQUEST_CODE = 28391

    // Add On Page Source
    const val ADDON_PAGE_SOURCE_PDP = "pdp"
    const val ADDON_PAGE_SOURCE_CART = "cart"
    const val ADDON_PAGE_SOURCE_CHECKOUT = "checkout"

    object TrackerId {
        const val CLICK_BUNDLE_OPTIONS = "20007"
        const val CLICK_SEE_PRODUCT = "20008"
        const val CLICK_CHOOSE_PRODUCT_VARIANT = "20009"
        const val ADD_TO_CART_BUNDLING = "20010"
        const val CLICK_BACK ="20011"
    }
}
