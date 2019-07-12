package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConstInternalPromo {

    const val HOST_PROMO = "promo"

    const val INTERNAL_PROMO = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_PROMO}"

    const val PROMO_LIST_DIGITAL = "${INTERNAL_PROMO}/list/digital"
}