package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

/**
 * This class is used to store deeplink "tokopedia-android-internal://merchant"
 */

object ApplinkConstInternalMechant {
    const val QUERY_PARAM_ID = "id"
    const val QUERY_PARAM_MODE = "mode"
    const val MODE_EDIT_PRODUCT = "edit-product"
    const val MODE_EDIT_DRAFT = "edit-draft"
    const val MODE_DUPLICATE_PRODUCT = "duplicate-product"
    const val HOST_MERCHANT = "merchant"

    const val INTERNAL_MERCHANT = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_MERCHANT}"

    const val MERCHANT_REDIRECT_CREATE_SHOP = "${INTERNAL_MERCHANT}:/redirect-create-shop"

    const val MERCHANT_OPEN_CATALOG_PICKER = "${INTERNAL_MERCHANT}:/open-catalog-picker"

    const val MERCHANT_OPEN_PRODUCT_PREVIEW = "${INTERNAL_MERCHANT}/open-product-preview"

    const val MERCHANT_PRODUCT_DRAFT = "${INTERNAL_MERCHANT}/product-draft"

    // Official Store Brandlist
    const val BRANDLIST = "${INTERNAL_MERCHANT}/official-store/brand/{category_id}/"
  
    // Official Store Brandlist - Search Page
    const val BRANDLIST_SEARCH = "${INTERNAL_MERCHANT}/official-store/brand-search"

    const val MERCHANT_SHOP_SHOWCASE_LIST = "${INTERNAL_MERCHANT}/shop-showcase-list"

    const val MERCHANT_SHOP_SHOWCASE_ADD = "${INTERNAL_MERCHANT}/shop-showcase-add"

    const val MERCHANT_SHOP_SHOWCASE_EDIT = "${INTERNAL_MERCHANT}/shop-showcase-edit"

    const val MERCHANT_SHOP_SCORE = "${INTERNAL_MERCHANT}/shop-score-detail"

    //com.tokopedia.statistic.presentation.view.activity.StatisticActivity
    const val MERCHANT_STATISTIC_DASHBOARD = "$INTERNAL_MERCHANT/statistic_dashboard"
}
