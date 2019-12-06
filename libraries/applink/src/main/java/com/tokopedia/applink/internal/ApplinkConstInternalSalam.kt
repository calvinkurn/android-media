package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConstInternalSalam{
    const val HOST_SALAM = "order-details"


    const val INTERNAL_SALAM = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://${HOST_SALAM}"

    const val SALAM_ORDER_DETAIL = "$INTERNAL_SALAM/umroh/.*\\"
}