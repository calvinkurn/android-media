package com.tokopedia.applink.etalase

import android.net.Uri
import com.tokopedia.applink.internal.ApplinkConstInternalMechant

/**
 * Created By @ilhamsuaib on 14/09/20
 */

object DeepLinkMapperEtalase {

    const val PATH_SHOP_ID = "shop_id"
    private const val PATH_ETALASE_LIST = "etalase-list"

    /**
     * @param deepLink tokopedia://shop/12345/etalase-list
     * @return tokopedia-android-internal://shop-showcase-list?shop_id=12345
     * or will return empty string if given invalid deep link
     * */
    fun getEtalaseListInternalAppLink(deepLink: String): String {
        val uri = Uri.parse(deepLink)
        return when {
            uri.pathSegments.size == 2 && uri.pathSegments[1] == PATH_ETALASE_LIST -> {
                val shopId: String = if (!uri.pathSegments[0].isNullOrBlank()) {
                    uri.pathSegments[0]
                } else {
                    "0"
                }
                Uri.parse(ApplinkConstInternalMechant.MERCHANT_SHOP_SHOWCASE_LIST)
                        .buildUpon()
                        .appendQueryParameter(PATH_SHOP_ID, shopId)
                        .build()
                        .toString()
            }
            else -> ""
        }
    }
}