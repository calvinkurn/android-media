package com.tokopedia.applink.internal

import com.tokopedia.applink.ApplinkConst
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



    private const val HOST_TOKOPOINTS = "tokopoints"
    const val INTERNAL_TOKOPOINTS = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_TOKOPOINTS}"
    const val TOKOPOINTS_HOME = "${INTERNAL_TOKOPOINTS}/home"
    const val TOKOPOINTS_COUPON_LISTING = "${INTERNAL_TOKOPOINTS}/kupon-saya/{slug}"
    const val TOKOPOINTS_COUPON_DETAIL = "${INTERNAL_TOKOPOINTS}/kupon-detail/{coupon_code}"
    const val TOKOPOINTS_COUPON = "${INTERNAL_TOKOPOINTS}/kupon-saya"
    const val TOKOPOINTS_CATALOG_DETAIL = "${INTERNAL_TOKOPOINTS}/tukar-detail/{catalog_code}"
    const val TOKOPOINTS_CATALOG_LISTING = "${INTERNAL_TOKOPOINTS}/tukar-point/{slug_category}/{slug_sub_category}"
}