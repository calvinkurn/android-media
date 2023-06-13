package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

/**
 * @author by jessica on 16/07/20

 */

object ApplinkConstInternalDeals {

    const val HOST_DEALS = "deals"

    const val INTERNAL_DEALS = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_DEALS"

    const val DEALS_HOMEPAGE = "$INTERNAL_DEALS/home-new"

    const val DEALS_CATEGORY_PAGE = "$INTERNAL_DEALS/category-new/page"

    const val DEALS_BRAND_PAGE = "$INTERNAL_DEALS/brand-new/page"

    const val DEALS_BRAND_DETAIL_PAGE = "$INTERNAL_DEALS/brand-detail-new"

    const val DEALS_PRODUCT_DETAIL_PAGE = "$INTERNAL_DEALS/pdp-new"

    const val DEALS_PROMO = "tokopedia://discovery/deals?activeTab=7&componentID=588"
}
