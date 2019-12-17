package com.tokopedia.product.detail.common

import com.tokopedia.url.TokopediaUrl

object ProductDetailCommonConstant{
    const val PARAM_PRODUCT_ID = "productID"
    const val PARAMS = "params"
    const val PARAM_SHOP_ID = "shopID"
    const val PARAM_SHOP_DOMAIN = "shopDomain"
    const val PARAM_PRODUCT_KEY = "productKey"
    const val PARAM_INPUT = "input"
    const val PARAM_CATALOG_ID = "catalogId"
    const val PARAM_PRODUCT_PRICE = "price"
    const val PARAM_PRODUCT_QUANTITY = "quantity"

    const val PARAM_SHOP_IDS = "shopIds"
    const val PARAM_SHOP_FIELDS = "fields"

    const val PARAM_RATE_EST_SHOP_DOMAIN = "domain"
    const val PARAM_RATE_EST_WEIGHT = "weight"

    const val PARAM_PAGE = "page"
    const val PARAM_TOTAL = "total"
    const val PARAM_CONDITION = "condition"
    const val PARAM_PRODUCT_TITLE = "productTitle"

    const val DEFAULT_NUM_VOUCHER = 3
    const val DEFAULT_NUM_IMAGE_REVIEW = 4

    val DEFAULT_SHOP_FIELDS = listOf("core", "favorite", "assets", "shipment",
            "last_active", "location", "terms", "allow_manage",
            "is_owner", "other-goldos", "status")

    const val SHOP_ID_PARAM = "shopId"
    const val PRODUCT_ID_PARAM = "productId"
    const val INCLUDE_UI_PARAM = "includeUI"

    val URL_APPLY_LEASING =   "${TokopediaUrl.getInstance().WEB}kredit-motor/kalkulator?productID=%s"

}