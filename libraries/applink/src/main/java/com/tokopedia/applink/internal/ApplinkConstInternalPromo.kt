package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConstInternalPromo {

    const val HOST_PROMO = "promo"

    const val INTERNAL_PROMO = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_PROMO}"

    const val PROMO_LIST_DIGITAL = "${INTERNAL_PROMO}/list/digital"
    const val PROMO_DETAIL_DIGITAL = "${INTERNAL_PROMO}/detail/digital"

    const val PROMO_LIST_FLIGHT = "${INTERNAL_PROMO}/list/flight"
    const val PROMO_DETAIL_FLIGHT = "${INTERNAL_PROMO}/detail/flight"

    const val PROMO_LIST_MARKETPLACE = "${INTERNAL_PROMO}/list/marketplace"
    const val PROMO_DETAIL_MARKETPLACE = "${INTERNAL_PROMO}/detail/marketplace"

}