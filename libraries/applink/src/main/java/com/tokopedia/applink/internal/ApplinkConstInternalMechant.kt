package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

/**
 * This class is used to store deeplink "tokopedia-android-internal://merchant"
 */

object ApplinkConstInternalMechant {
    @JvmField
    val QUERY_PARAM_ID = "id"
    @JvmField
    val QUERY_PARAM_MODE = "mode"
    @JvmField
    val MODE_EDIT_PRODUCT = "edit-product"
    @JvmField
    val MODE_EDIT_DRAFT = "edit-draft"
    @JvmField
    val MODE_DUPLICATE_PRODUCT = "duplicate-product"
    @JvmField
    val HOST_MERCHANT = "merchant"
  
    @JvmField
    val INTERNAL_MERCHANT = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_MERCHANT}"
  
    @JvmField
    val MERCHANT_REDIRECT_CREATE_SHOP = "${INTERNAL_MERCHANT}:/redirect-create-shop"
  
    @JvmField
    val MERCHANT_OPEN_CATALOG_PICKER = "${INTERNAL_MERCHANT}:/open-catalog-picker"
  
    @JvmField
    val MERCHANT_OPEN_PRODUCT_PREVIEW = "${INTERNAL_MERCHANT}/open-product-preview"

    @JvmField
    val MERCHANT_PRODUCT_DRAFT = "${INTERNAL_MERCHANT}/product-draft"

    // Official Store Brandlist
    @JvmField
    val BRANDLIST = "${INTERNAL_MERCHANT}/official-store/brand/{category_id}/"
  
    // Official Store Brandlist - Search Page
    @JvmField
    val BRANDLIST_SEARCH = "${INTERNAL_MERCHANT}/official-store/brand-search"
  
    @JvmField
    val MERCHANT_SHOP_SHOWCASE_LIST = "${INTERNAL_MERCHANT}/shop-showcase-list"

    @JvmField
    val MERCHANT_SHOP_SHOWCASE_ADD = "${INTERNAL_MERCHANT}/shop-showcase-add"

    @JvmField
    val MERCHANT_SHOP_SHOWCASE_EDIT = "${INTERNAL_MERCHANT}/shop-showcase-edit"

    @JvmField
    val MERCHANT_SHOP_SCORE = "${INTERNAL_MERCHANT}/shop-score-detail"

    //com.tokopedia.statistic.presentation.view.activity.StatisticActivity
    @JvmField
    val MERCHANT_STATISTIC_DASHBOARD = "$INTERNAL_MERCHANT/statistic_dashboard"
}
