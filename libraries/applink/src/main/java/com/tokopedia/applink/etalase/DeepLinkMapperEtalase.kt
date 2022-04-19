package com.tokopedia.applink.etalase

import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMechant

/**
 * Created By @ilhamsuaib on 14/09/20
 */

object DeepLinkMapperEtalase {

    const val PATH_SHOP_ID = "shop_id"
    private const val PATH_ETALASE_LIST = "etalase-list"

    fun getEtalaseListInternalAppLink(shopId: String?): String {
        return UriUtil.buildUriAppendParams(ApplinkConstInternalMechant.MERCHANT_SHOP_SHOWCASE_LIST,
                mapOf(PATH_SHOP_ID to (shopId ?: "0" )))
    }
}