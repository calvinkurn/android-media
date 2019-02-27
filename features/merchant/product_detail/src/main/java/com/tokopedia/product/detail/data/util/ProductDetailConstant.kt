package com.tokopedia.product.detail.data.util

import android.support.annotation.IntDef

object ProductDetailConstant {
    @JvmField
    var BASE_REST_URL = "https://ws.tokopedia.com/"

    const val MAINAPP_SHOW_PDP_TOPADS = "mainapp_show_pdp_topads"

    @IntDef(BUY, ADD_TO_CART, BUY_NOW)
    @Retention(AnnotationRetention.SOURCE)
    annotation class ProductAction {}

    const val BUY = 0
    const val ADD_TO_CART = 1
    const val BUY_NOW = 2

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
}