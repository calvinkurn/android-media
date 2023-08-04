package com.tokopedia.purchase_platform.common.constant

object AddOnConstant {
    const val EXTRA_ADD_ON_PRODUCT_DATA = "ADD_ON_PRODUCT_DATA"
    const val EXTRA_ADD_ON_PRODUCT_DATA_RESULT = "ADD_ON_PRODUCT_DATA_RESULT"
    const val EXTRA_ADD_ON_SOURCE = "ADD_ON_SOURCE"

    // For gallery activity
    const val EXTRA_ADD_ON_IMAGES = "ADD_ON_IMAGES"
    const val EXTRA_ADD_ON_NAME = "ADD_ON_NAME"
    const val EXTRA_ADD_ON_PRICE = "ADD_ON_PRICE"

    const val ADD_ONS_PRODUCT_SERVICE = "add_ons"
    const val ADD_ON_LEVEL_PRODUCT = "product"
    const val ADD_ON_LEVEL_ORDER = "order"
    const val ADD_ON_SOURCE_OCC = "order summary"
    const val ADD_ON_SOURCE_CHECKOUT = "courier selection"

    const val SOURCE_NORMAL_CHECKOUT = "normal"
    const val SOURCE_ONE_CLICK_SHIPMENT = "ocs"
    const val SOURCE_ONE_CLICK_CHECKOUT = "occ"

    const val ADD_ON_PRODUCT_STATUS_DEFAULT = 0
    const val ADD_ON_PRODUCT_STATUS_CHECK = 1
    const val ADD_ON_PRODUCT_STATUS_UNCHECK = 2
    const val ADD_ON_PRODUCT_STATUS_MANDATORY = 3

    const val QUERY_PARAM_PAGE_ATC_SOURCE = "atcSource"
    const val QUERY_PARAM_CART_ID = "cartId"
    const val QUERY_PARAM_SELECTED_ADDON_IDS = "selectedAddonIds"
    const val QUERY_PARAM_IS_TOKOCABANG = "isTokoCabang"
    const val QUERY_PARAM_ADDON_PRODUCT = "{addon_id}"
    const val QUERY_PARAM_WAREHOUSE_ID = "warehouseId"
    const val QUERY_PARAM_CATEGORY_ID = "categoryId"
    const val QUERY_PARAM_SHOP_ID = "shopId"
    const val QUERY_PARAM_QUANTITY = "quantity"
    const val QUERY_PARAM_PRICE = "price"
    const val QUERY_PARAM_DISCOUNTED_PRICE = "discountedPrice"
    const val CONDITIONS = "conditions"

    const val PRODUCT_PROTECTION_INSURANCE_TYPE = 4
}
