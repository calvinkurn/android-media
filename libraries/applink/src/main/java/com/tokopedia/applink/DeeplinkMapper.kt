package com.tokopedia.applink

import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace

object DeeplinkMapper {

    /**
     * Get registered deeplink navigation in manifest
     */
    @Deprecated("")
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
        return when (deeplink) {
            else -> ""
        }
    }

    /**
     * Mapping tokopedia link to registered deplink in manifest if necessary
     * eg: tokopedia://product/add to tokopedia-android-internal://marketplace/product-add-item
     * If not found, return current deeplink, means it registered
     *
     * Update:
     * If possible, do not do any mapping here.
     * If scheme tokopedia exists,
     * Just register tokopedia:// into manifest and do not use tokopedia-android-internal://
     *
     * For example, "tokopedia://inbox" already exists in Applink Const,
     * put in manifest of InboxActivity. No need to create internal.
     * Also remove that @Deeplink("tokopedia://inbox") for airbnb.
     */
    @Deprecated("")
    private fun getRegisteredNavigationFromTokopedia(deeplink: String): String {
        return when (deeplink) {
            else -> ""
        }
    }

    /**
     * Mapping sellerapp link to registered deplink in manifest if necessary
     * eg: sellerapp://product/add to tokopedia-android-internal://marketplace/product-add-item
     * If not found, return current deeplink, means it registered
     *
     * Update:
     * If possible, do not do any mapping here.
     * If scheme sellerapp exists,
     * Just register sellerapp:// into manifest and do not use tokopedia-android-internal://
     *
     * For example, "sellerapp://product/add" already exists in Applink Const,
     * put in manifest of ProductAddActivity. No need to create internal.
     * Also remove that @Deeplink("sellerapp://product/add") for airbnb.
     *
     * If the format is different betweek original deeplink and internal deeplink, mapping is okay..
     */
    @Deprecated("")
    private fun getRegisteredNavigationFromSellerapp(deeplink: String): String {
        return when (deeplink) {
            else -> ""
        }
    }
}