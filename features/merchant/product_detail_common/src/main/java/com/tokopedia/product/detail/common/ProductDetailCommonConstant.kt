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
    const val PARAM_PRICE = "price"
    const val PARAM_CATEGORY_ID = "categoryId"
    const val PARAM_USER_ID = "userId"
    const val PARAM_MIN_ORDER = "minOrder"
    const val PARAM_WAREHOUSE_ID = "wareHouseId"
    const val PARAM_TRADE_IN = "tradeIn"
    const val FORCE_REFRESH = "forceRefresh"

    const val PARAM_SHOP_IDS = "shopIds"
    const val PARAM_SHOP_FIELDS = "fields"

    const val PARAM_RATE_EST_SHOP_DOMAIN = "domain"
    const val PARAM_RATE_EST_WEIGHT = "weight"

    const val PARAM_PAGE = "page"
    const val PARAM_TOTAL = "total"
    const val PARAM_CONDITION = "condition"
    const val PARAM_PRODUCT_TITLE = "productTitle"
    const val PARAM_PRODUCT_ORIGIN = "origin"
    const val PARAM_IS_PDP = "isPDP"

    const val DEFAULT_NUM_VOUCHER = 3
    const val DEFAULT_NUM_IMAGE_REVIEW = 4
    const val IS_ALLOW_MANAGE = 1

    val DEFAULT_SHOP_FIELDS = listOf("core", "favorite", "assets", "shipment",
            "last_active", "location", "terms", "allow_manage",
            "is_owner", "other-goldos", "status")

    const val SHOP_ID_PARAM = "shopId"
    const val PRODUCT_ID_PARAM = "productId"
    const val INCLUDE_UI_PARAM = "includeUI"

    val URL_APPLY_LEASING =   "${TokopediaUrl.getInstance().WEB}kredit-motor/kalkulator?productID=%s"

    //Tradein
    const val PARAM_TRADEIN_CATEGORY_ID = "CategoryId"
    const val PARAM_TRADEIN_DEVICE_ID = "DeviceId"
    const val PARAM_TRADEIN_IS_ELIGIBLE = "isEligible"
    const val PARAM_TRADEIN_IS_ON_CAMPAIGN = "IsOnCampaign"
    const val PARAM_TRADEIN_IS_PRE_ORDER = "IsPreOrder"
    const val PARAM_TRADEIN_MODEL_ID = "ModelId"
    const val PARAM_TRADEIN_NEW_PRICE = "NewPrice"
    const val PARAM_TRADEIN_PRODUCT_ID= "ProductId"
    const val PARAM_TRADEIN_PRODUCT_NAME = "productName"
    const val PARAM_TRADEIN_REMAINING_PRICE = "remainingPrice"
    const val PARAM_TRADEIN_SHOP_ID = "ShopId"
    const val PARAM_TRADEIN_TRADE_IN_TYPE = "TradeInType"
    const val PARAM_TRADEIN_TRADE_USE_KYC = "useKyc"
    const val PARAM_TRADEIN_TRADE_USED_PRICE = "usedPrice"
    const val PARAM_TRADEIN_TRADE_USER_ID = "UserId"

}