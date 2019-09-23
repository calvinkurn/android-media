package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConsInternalDigital {

    const val HOST_DIGITAL = "digital"
    const val HOST_RECHARGE = "recharge"
    const val HOME_RECHARGE = "home"

    const val INTERNAL_DIGITAL = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_DIGITAL}"
    const val INTERNAL_DIGITAL_HOME = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_RECHARGE}/${HOME_RECHARGE}"

    const val CART_DIGITAL = "${INTERNAL_DIGITAL}/cart"
    const val TELCO_DIGITAL = "${INTERNAL_DIGITAL}/telco"
    const val VOUCHER_GAME = "${INTERNAL_DIGITAL}/vouchergame"
}