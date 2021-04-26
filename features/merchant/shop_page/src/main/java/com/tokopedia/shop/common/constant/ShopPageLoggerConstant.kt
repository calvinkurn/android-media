package com.tokopedia.shop.common.constant

/**
 * Created by hendry on 18/07/18.
 */
object ShopPageLoggerConstant {
    object Tag{
        const val SHOP_PAGE_HEADER_BUYER_FLOW_TAG = "BUYER_FLOW_SHOP_HEADER_STATUS"
        const val SHOP_PAGE_HOME_TAB_BUYER_FLOW_TAG = "BUYER_FLOW_SHOP_HOME_STATUS"
        const val SHOP_PAGE_PRODUCT_TAB_BUYER_FLOW_TAG = "BUYER_FLOW_SHOP_PRODUCT_STATUS"
        const val SHOP_PAGE_PRODUCT_SEARCH_BUYER_FLOW_TAG = "BUYER_FLOW_SHOP_PRODUCT_SEARCH_STATUS"
        const val SHOP_PAGE_PRODUCT_RESULT_BUYER_FLOW_TAG = "BUYER_FLOW_SHOP_PRODUCT_RESULT_STATUS"
    }

    object EXTRA_PARAM_KEY{
        const val FUNCTION_NAME_KEY = "function_name"
        const val LIVE_DATA_NAME_KEY = "livedata_name"
        const val SHOP_ID_KEY = "shop_id"
        const val USER_ID_KEY = "user_id"
        const val SHOP_NAME_KEY = "shop_name"
        const val REASON_KEY = "reason"
        const val DATA_KEY = "data"
    }

}