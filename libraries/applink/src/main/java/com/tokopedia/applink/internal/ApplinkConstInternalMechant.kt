package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

/**
 * This class is used to store deeplink "tokopedia-android-internal://merchant"
 */

object ApplinkConstInternalMechant {
    // Product Bundle Query Params
    const val QUERY_PARAM_BUNDLE_ID = "bundleId"
    const val QUERY_PARAM_PAGE_SOURCE = "source"
    const val QUERY_PARAM_WAREHOUSE_ID = "warehouseId"
    const val SOURCE_PDP = "pdp"
    const val SOURCE_SHOP_PAGE = "shop-page"

    // Add Edit Product Query Params
    const val QUERY_PARAM_ID = "id"
    const val QUERY_PARAM_MODE = "mode"
    const val MODE_EDIT_PRODUCT = "edit-product"
    const val MODE_EDIT_DRAFT = "edit-draft"
    const val MODE_DUPLICATE_PRODUCT = "duplicate-product"

    const val HOST_MERCHANT = "merchant"

    const val INTERNAL_MERCHANT = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_MERCHANT"

    const val MERCHANT_REDIRECT_CREATE_SHOP = "$INTERNAL_MERCHANT/redirect-create-shop"

    const val MERCHANT_OPEN_PRODUCT_PREVIEW = "$INTERNAL_MERCHANT/open-product-preview"

    const val MERCHANT_PRODUCT_DRAFT = "$INTERNAL_MERCHANT/product-draft"

    // Product Service Widget
    const val MERCHANT_PRODUCT_BUNDLE = "$INTERNAL_MERCHANT/product-bundle/{product_id}/"
    const val MERCHANT_GIFTING = "$INTERNAL_MERCHANT/gifting/{addon_id}/"

    // Official Store Brandlist
    const val BRANDLIST = "$INTERNAL_MERCHANT/official-store/brand/{category_id}/"

    // Official Store Brandlist - Search Page
    const val BRANDLIST_SEARCH = "$INTERNAL_MERCHANT/official-store/brand-search"

    const val MERCHANT_SHOP_SHOWCASE_LIST = "$INTERNAL_MERCHANT/shop-showcase-list"

    const val MERCHANT_SHOP_SHOWCASE_ADD = "$INTERNAL_MERCHANT/shop-showcase-add"

    const val MERCHANT_SHOP_SCORE = "$INTERNAL_MERCHANT/shop-score-detail"

    // com.tokopedia.statistic.presentation.view.activity.StatisticActivity
    const val MERCHANT_STATISTIC_DASHBOARD = "$INTERNAL_MERCHANT/statistic_dashboard"

    const val SHOP_NIB_CUSTOMER_APP = "$INTERNAL_MERCHANT/shop-nib"

    const val BUY_MORE_GET_MORE_OLP = "$INTERNAL_MERCHANT/buy-more-get-more/{shop_id}/olp/{offer_id}/"
}
