package com.tokopedia.product.detail.common

import com.tokopedia.url.TokopediaUrl

object ProductDetailCommonConstant{
    const val PARAM_PRODUCT_ID = "productID"
    const val PARAM_PDP_SESSION = "pdpSession"
    const val PARAM_SHOP_ID = "shopID"
    const val PARAM_SHOP_DOMAIN = "shopDomain"
    const val PARAM_PRODUCT_KEY = "productKey"
    const val PARAM_DEVICE_ID = "deviceID"
    const val PARAM_WAREHOUSE_ID = "whID"
    const val PARAM_LAYOUT_ID = "layoutID"
    const val PARAM_INPUT = "input"
    const val PARAM_IS_SHOP_OWNER = "isShopOwner"
    const val PARAM_USER_LOCATION = "userLocation"

    const val PARAM_SHOP_IDS = "shopIds"

    const val PARAM_RATE_EST_SHOP_DOMAIN = "domain"
    const val PARAM_RATE_EST_WEIGHT = "weight"

    const val PARAM_PAGE = "page"
    const val PARAM_TOTAL = "total"
    const val PARAM_PRODUCT_ORIGIN = "origin"

    const val DEFAULT_NUM_IMAGE_REVIEW = 5

    const val SHOP_ID_PARAM = "shopId"
    const val FIELDS_PARAM = "fields"
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
    const val PARAM_APPLINK_AVAILABLE_VARIANT = "available variants"

    const val KEY_NORMAL_BUTTON = "normal"
    const val KEY_OCS_BUTTON = "ocs"
    const val KEY_OCC_BUTTON = "occ"
    const val KEY_CHAT = "chat"
    const val KEY_BYME = "byme"
    const val KEY_REMIND_ME = "remind_me"
    const val KEY_CHECK_WISHLIST = "check_wishlist"
    const val KEY_BUTTON_PRIMARY = "primary"
    const val KEY_BUTTON_PRIMARY_GREEN = "primary_green"
    const val KEY_BUTTON_SECONDARY_GREEN = "secondary_green"
    const val KEY_BUTTON_DISABLE = "disabled"
    const val KEY_BUTTON_SECONDARY = "secondary"
    const val KEY_BUTTON_SECONDARY_GRAY = "secondary_gray"
    const val KEY_CART_TYPE_REMIND_ME = "remind_me"
    const val KEY_CART_TYPE_CHECK_WISHLIST = "check_wishlist"

    //Button Action
    const val BUY_BUTTON = 1
    const val ATC_BUTTON = 2
    const val OCS_BUTTON = 3
    const val OCC_BUTTON = 4
    const val TRADEIN_BUTTON = 6
    const val TRADEIN_AFTER_DIAGNOSE = 7
    const val REMIND_ME_BUTTON = 8
    const val CHECK_WISHLIST_BUTTON = 9
    const val ATC_UPDATE_BUTTON = 10

    const val REQUEST_CODE_CHECKOUT = 12382

    //OVO
    const val OVO_INACTIVE_STATUS = 1;
    const val OVO_INSUFFICIENT_BALANCE_STATUS = 2;

}