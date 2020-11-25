package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

/**
 * @author by jessica on 16/07/20

 */

object ApplinkConstInternalDeals {

    @JvmField
    val HOST_DEALS = "deals"

    @JvmField
    val INTERNAL_DEALS = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_DEALS"

    @JvmField
    val DEALS_HOMEPAGE = "$INTERNAL_DEALS/home-new"

    @JvmField
    val DEALS_CATEGORY_PAGE = "$INTERNAL_DEALS/category-new/page"

    @JvmField
    val DEALS_BRAND_PAGE = "$INTERNAL_DEALS/brand-new/page"

    const val DEALS_PROMO = "tokopedia://promoNative?menuID=4&categoryID=371"
}