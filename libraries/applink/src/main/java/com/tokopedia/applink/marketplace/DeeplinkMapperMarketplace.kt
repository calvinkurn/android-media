package com.tokopedia.applink.marketplace

import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace

/**
 * Created by Irfan Khoirul on 2019-10-08.
 */

object DeeplinkMapperMarketplace {

    fun getRegisteredNavigationMarketplace(deeplink: String): String {
        if (deeplink.startsWith(ApplinkConst.CART)) {
            return ApplinkConstInternalMarketplace.CART
        } else if (deeplink.startsWith(ApplinkConst.CHECKOUT)) {
            return deeplink.replace(ApplinkConst.CHECKOUT, ApplinkConstInternalMarketplace.CHECKOUT)
        }
        return deeplink
    }

}