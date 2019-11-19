package com.tokopedia.product.detail.common

import com.tokopedia.url.TokopediaUrl

object ProductDetailCommonConstant{
    const val PARAM_PRODUCT_ID = "productID"
    const val PARAM_SHOP_ID = "shopID"
    const val PARAM_SHOP_DOMAIN = "shopDomain"
    const val PARAM_PRODUCT_KEY = "productKey"
    const val PARAM_INPUT = "input"
    const val PARAM_CATALOG_ID = "catalogId"
    const val PARAM_PRODUCT_PRICE = "price"
    const val PARAM_PRODUCT_QUANTITY = "quantity"

    val URL_APPLY_LEASING =   "${TokopediaUrl.getInstance().WEB}kredit-motor/kalkulator?productID=%s"

}