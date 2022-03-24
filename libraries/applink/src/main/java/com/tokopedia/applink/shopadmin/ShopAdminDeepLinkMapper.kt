package com.tokopedia.applink.shopadmin

import android.net.Uri
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace

object ShopAdminDeepLinkMapper {
    const val SHOP_NAME = "shop_name"

    /**
     * mapping applink external to internal
     * e.g, external
     * MA with param: tokopedia://shop-admin/accepted-page?shop_name=toko
     * SA with param: sellerapp://shop-admin/accepted-page?shop_name=toko
     * to Internal Applink
     * with param: tokopedia-android-internal://marketplace/shop-admin/accepted-page?shop_name=toko
     */
    fun getInternalAppLinkAdminAccepted(uri: Uri): String {
        val shopName = uri.getQueryParameter(SHOP_NAME).orEmpty()
        return if (shopName.isEmpty()) {
            ApplinkConstInternalMarketplace.ADMIN_ACCEPTED
        } else {
            val params = mapOf<String, Any>(SHOP_NAME to shopName)
            UriUtil.buildUriAppendParams(ApplinkConstInternalMarketplace.ADMIN_ACCEPTED, params)
        }
    }
}