package com.tokopedia.applink

import com.tokopedia.applink.internal.ApplinkConstInternal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace

object DeeplinkMapper {

    fun getDeeplinkTokopedia(externalLink: String): String {
        return ""
    }

    /**
     * Get / mapping internal deeplink from deeplink tokopedia
     * Ideal case deeplink tokopedia will be pit directly in manifest, without this mapping
     *
     * eg: tokopedia://product/{product_id} -> tokopedia-android-internal://product-detail/{product_id}
     */
    fun getDeeplinkTokopediaInternal(deeplinkTokopedia: String): String {
        // It's already internal scheme, cannot mapping with this function, return nothing
        if (deeplinkTokopedia.startsWith(ApplinkConstInternal.INTERNAL_SCHEME)) {
            return ""
        }
        when (deeplinkTokopedia) {
            ApplinkConst.PRODUCT_INFO -> return ApplinkConstInternalMarketplace.PRODUCT_DETAIL
            ApplinkConst.PRODUCT_ADD -> return ApplinkConstInternalMarketplace.PRODUCT_ADD_ITEM
            ApplinkConst.SellerApp.PRODUCT_ADD -> return ApplinkConstInternalMarketplace.PRODUCT_ADD_ITEM
        }
        return ""
    }
}

