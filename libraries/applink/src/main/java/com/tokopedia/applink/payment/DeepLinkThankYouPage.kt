package com.tokopedia.applink.payment

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalPayment

object DeepLinkThankYouPage {
    fun getThankYouPage(deepLink: String): String {
        if (deepLink.startsWith(ApplinkConst.THANK_YOU_PAGE_NATIVE)) {
            return deepLink.replace(ApplinkConst.THANK_YOU_PAGE_NATIVE, ApplinkConstInternalPayment.PAYMENT_THANK_YOU_PAGE)
        }else if(deepLink.startsWith(ApplinkConst.THANK_YOU_PAGE_NATIVE))
            return deepLink.replace(ApplinkConst.THANK_YOU_PAGE_NATIVE, ApplinkConstInternalPayment.PAYMENT_THANK_YOU_PAGE)
        return deepLink
    }
}