package com.tokopedia.applink.productmanage

import android.net.Uri
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace

/**
 * Created By @ilhamsuaib on 01/04/20
 */

object DeepLinkMapperProductManage {

    /**
     * @param deepLink : tokopedia://product/edit/12345
     * @return tokopedia-android-internal://marketplace/product-edit-item/12345/
     * */
    fun getEditProductInternalAppLink(deepLink: String): String {
        val uri = Uri.parse(deepLink)
        return when {
            //tokopedia://product/edit/12345
            uri.pathSegments.size == 2 && uri.pathSegments[0] == "edit" -> {
                val productId = uri.pathSegments[1]
                UriUtil.buildUri(ApplinkConstInternalMarketplace.PRODUCT_EDIT_ITEM, productId)
            }
            else -> ""
        }
    }
}