package com.tokopedia.analytics.byteio

internal val TAG = "BYTEIO"

data class TrackProduct(
    val productId: String,
    val isAd: Boolean,
    val orderFrom1: Int,
    val requestId: String? = ""
)

data class TrackProductDetail(
    val productId: String,
    val productCategory: String,
//    val entrance_info: TODO
    val productType: ProductType,
    val originalPrice: String,
    val salePrice: String,
)

data class TrackStayProductDetail(
    val productId: String,
    val productCategory: String,
//    val entrance_info: TODO
    val productType: ProductType,
    val originalPrice: String,
    val salePrice: String,
    val isLoadData: Boolean,
    val isSkuSelected: Boolean,
    val isAddCartSelected: Boolean,
)

data class TrackConfirmSku(
    val productId: String,
    val productCategory: String,
//    val entrance_info: TODO
    val productType: ProductType,
    val originalPrice: String,
    val salePrice: String,
    val skuId: String,
    val currency: String = "IDR",
    val isSkuSelected: Boolean,
    val isAddCartSelected: Boolean,
    val qty: String,
    val isHaveAddress: Boolean,
)

data class TrackConfirmCart(
    val productId: String,
    val productCategory: String,
//    val entrance_info: TODO
    val productType: ProductType,
    val originalPrice: String,
    val salePrice: String,
    val skuId: String,
    val currency: String = "IDR",
    val addSkuNum: Int,
    val skuNumBefore: Int = 0, // need development from BE
    val skuNumAfter: Int = 0 // need development from BE
)

data class TrackConfirmCartResult(
    val productId: String,
    val productCategory: String,
//    val entrance_info: TODO
    val productType: ProductType,
    val originalPrice: String,
    val salePrice: String,
    val skuId: String,
    val currency: String = "IDR",
    val addSkuNum: Int,
    val skuNumBefore: Int = 0, // need development from BE
    val skuNumAfter: Int = 0, // need development from BE
    val isSuccess: String,
    val failReason: String
)


enum class ProductType(val type: Int) {
    AVAILABLE(1),
    SOLD_OUT(2),
    NOT_AVAILABLE(3),
    LIVE_REGION_NOT_AVAILABLE(4),
    NON_LIVE_REGION_NOT_AVAILABLE_OR_REMOVED(5),
}

object PageName {
    const val MAINPAGE = "mall"
    const val PDP = "product_detail"
    const val SEARCH_RESULT = "search_result"
    const val CART = "cart"
    const val SHOP = "shop"
}

object QuitType {
    const val RETURN = "return"
    const val NEXT = "next"
    const val CLOSE = "close"
}


enum class EntranceForm(val str: String) {
    HORIZONTAL_GOODS_CARD("horizontal_goods_card"),
    GRID_GOODS_CARD("grid_goods_card")
}

enum class SourcePageType(val str: String) {
    HOME_FOR_YOU("mainPage_foryou"),
    TESTAPP_SOURCE("testapp_source")
}

object EventName {
    const val PRODUCT_SHOW = "tiktokec_product_show"
    const val PRODUCT_CLICK = "tiktokec_product_click"
    const val ENTER_PRODUCT_DETAIL = "tiktokec_enter_product_detail"
    const val STAY_PRODUCT_DETAIL = "tiktokec_stay_product_detail"
    const val CONFIRM_SKU = "tiktokec_confirm_sku"
    const val CONFIRM_CART = "tiktokec_confirm_cart"
    const val CONFIRM_CART_RESULT = "tiktokec_confirm_cart_result"
    const val SUBMIT_ORDER_RESULT = "tiktokec_submit_order_result"
}
