package com.tokopedia.applink.shopadmin

import android.net.Uri
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace

object ShopAdminDeepLinkMapper {

    const val ARGS_APPLINK_FROM_SHOP_ADMIN = "args_applink_from_shop_admin"
    const val ARGS_ERROR_MESSAGE_FROM_SHOP_ADMIN = "args_error_message_from_shop_admin"

    const val REQUEST_CODE_ADMIN_REDIRECTION_FROM_LANDING_SHOP = 939
    const val REQUEST_CODE_ADMIN_REDIRECTION_FROM_PHONE_SHOP = 875

    const val LANDING_SHOP_CREATION = "landing_shop_creation"
    const val PHONE_SHOP_CREATION = "phone_shop_creation"

    const val SHOP_NAME = "shop_name"
    const val FROM_PARAM = "from"

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