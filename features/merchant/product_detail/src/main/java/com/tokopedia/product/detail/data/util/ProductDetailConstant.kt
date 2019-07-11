package com.tokopedia.product.detail.data.util

object ProductDetailConstant {
    @JvmField
    var BASE_REST_URL = "https://ws.tokopedia.com/"

    const val MAINAPP_SHOW_PDP_TOPADS = "mainapp_show_pdp_topads"

    //report product
    const val PATH_PRODUCT_TYPE = "v4/product/"
    const val PATH_REPORT_TYPE = "get_product_report_type.pl"

    const val PATH_PRODUCT_ACTION = "v4/action/product/"
    const val PATH_REPORT = "report_product.pl"

    const val PATH_FAVORITE_SHOP_ACTION = "v4/action/favorite-shop/fav_shop.pl"

    const val PARAM_PRODUCT_ID = "product_id"
    const val PARAM_REPORT_TYPE = "report_type"
    const val PARAM_TEXT_MESSAGE = "text_message"

    const val PARAM_PRODUCT_ETALASE_ID = "product_etalase_id"
    const val PARAM_PRODUCT_ETALASE_NAME = "product_etalase_name"

    const val VALUE_NEW_ETALASE = "new"

    //waarehouse product
    const val PATH_MOVE_TO_WAREHOUSE = "move_to_warehouse.pl"
    const val PATH_MOVE_TO_ETALASE = "edit_etalase.pl"

    const val PARAM_PRICE = "price"

    const val URL_VALUE_PROPOSITION_READY ="https://www.tokopedia.com/help/article/a-1937"
    const val URL_VALUE_PROPOSITION_ORI ="https://www.tokopedia.com/help/article/a-1938"
    const val URL_VALUE_PROPOSITION_GUARANTEE ="https://www.tokopedia.com/help/article/a-1939"
    const val URL_GUARANTEE = "https://www.tokopedia.com/help/article/a-1940"


}