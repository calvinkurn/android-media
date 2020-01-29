package com.tokopedia.applink.order

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.startsWithPattern

/**
 * Created by fwidjaja on 2020-01-26.
 */
object DeeplinkMapperOrder {
    fun getRegisteredNavigationOrder(deeplink: String): String {
        return if (deeplink.startsWithPattern(ApplinkConst.SELLER_ORDER_DETAIL)) getRegisteredNavigationOrderInternal(deeplink)
        else deeplink
    }

    /**
     * tokopedia://seller/order/{order_id}
     */
    private fun getRegisteredNavigationOrderInternal(deeplink: String): String {
        return deeplink.replace(DeeplinkConstant.SCHEME_TOKOPEDIA, DeeplinkConstant.SCHEME_INTERNAL)
    }
}