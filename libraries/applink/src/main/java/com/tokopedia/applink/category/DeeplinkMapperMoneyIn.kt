package com.tokopedia.applink.category

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalCategory

object DeeplinkMapperMoneyIn {

    fun getRegisteredNavigationMoneyIn(deeplink: String): String {
        return deeplink.replace(ApplinkConst.MONEYIN, ApplinkConstInternalCategory.MONEYIN_INTERNAL)
    }
}