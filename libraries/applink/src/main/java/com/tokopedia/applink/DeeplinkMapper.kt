package com.tokopedia.applink

import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace

object DeeplinkMapper {

    /**
     * Get registered deeplink navigation in manifest
     */
    fun getRegisteredNavigation(deeplink: String): String {
        if (deeplink.startsWith(DeeplinkConstant.SCHEME_HTTP, true)) {
            return getRegisteredNavigationFromHttp(deeplink)
        } else if (deeplink.startsWith(DeeplinkConstant.SCHEME_TOKOPEDIA, true)) {
            return getRegisteredNavigationFromTokopedia(deeplink)
        } else if (deeplink.startsWith(DeeplinkConstant.SCHEME_SELLERAPP, true)) {
            return getRegisteredNavigationFromSellerapp(deeplink)
        } else {
            return deeplink
        }
    }

    /**
     * Mapping http link to registered deplink in manifest (to deeplink tokopedia or tokopedia-android-internal)
     * Due to no differentiation structure link for shop, product with other link (www.tokopedia.com/{shop_domain}/{product_name})
     * the app need translate
     * This function should be called after checking domain shop from server side
     * eg: https://www.tokopedia.com/pulsa/ to tokopedia://pulsa
     */
    private fun getRegisteredNavigationFromHttp(deeplink: String): String {
        when (deeplink) {

        }
        // Default deeplink, webview
        return ""
    }

    /**
     * Mapping tokopedia link to registered deplink in manifest if necessary
     * eg: tokopedia://product/add to tokopedia-android-internal://marketplace/product-add-item
     * If not found, return current deeplink, means it registered
     */
    private fun getRegisteredNavigationFromTokopedia(deeplink: String): String {
        when (deeplink) {
            ApplinkConst.PRODUCT_ADD -> return ApplinkConstInternalMarketplace.PRODUCT_ADD_ITEM
        }
        return ""
    }

    /**
     * Mapping sellerapp link to registered deplink in manifest if necessary
     * eg: sellerapp://product/add to tokopedia-android-internal://marketplace/product-add-item
     * If not found, return current deeplink, means it registered
     */
    private fun getRegisteredNavigationFromSellerapp(deeplink: String): String {
        when (deeplink) {
            ApplinkConst.SellerApp.PRODUCT_ADD -> return ApplinkConstInternalMarketplace.PRODUCT_ADD_ITEM
        }
        return ""
    }
}