package com.tokopedia.product.detail.common

import com.tokopedia.url.TokopediaUrl

object ProductDetailCommonConstant{
    const val PARAM_PRODUCT_ID = "productID"
    const val PARAM_PDP_SESSION = "pdpSession"
    const val PARAM_SHOP_ID = "shopID"
    const val PARAM_SHOP_DOMAIN = "shopDomain"
    const val PARAM_PRODUCT_KEY = "productKey"
    const val PARAM_WAREHOUSE_ID = "whID"
    const val PARAM_INPUT = "input"
    const val PARAM_CATALOG_ID = "catalogId"
    const val PARAM_IS_SHOP_OWNER = "isShopOwner"

    const val PARAM_SHOP_IDS = "shopIds"
    const val PARAM_NEED_REQUEST_COD = "needRequestCod"

    const val PARAM_RATE_EST_SHOP_DOMAIN = "domain"
    const val PARAM_RATE_EST_WEIGHT = "weight"

    const val PARAM_PAGE = "page"
    const val PARAM_TOTAL = "total"
    const val PARAM_PRODUCT_ORIGIN = "origin"
    const val PARAM_IS_PDP = "isPDP"

    const val DEFAULT_NUM_IMAGE_REVIEW = 4

    const val SHOP_ID_PARAM = "shopId"
    const val PRODUCT_ID_PARAM = "productId"
    const val INCLUDE_UI_PARAM = "includeUI"

    val URL_APPLY_LEASING =   "${TokopediaUrl.getInstance().WEB}kredit-motor/kalkulator?productID=%s"

    //notify me (teaser campaign)
    const val PARAM_TEASER_CAMPAIGN_ID = "campaignId"
    const val PARAM_TEASER_PRODUCT_ID = "productId"
    const val PARAM_TEASER_ACTION = "action"
    const val PARAM_TEASER_SOURCE = "source"
    const val VALUE_TEASER_ACTION_REGISTER = "REGISTER"
    const val VALUE_TEASER_ACTION_UNREGISTER = "UNREGISTER"
    const val VALUE_TEASER_TRACKING_REGISTER = "on"
    const val VALUE_TEASER_TRACKING_UNREGISTER = "off"
    const val VALUE_TEASER_SOURCE = "pdp"

    const val PARAM_APPLINK_SHOP_ID = "shop_id"
    const val PARAM_APPLINK_IS_VARIANT_SELECTED = "is_variant_selected"
}