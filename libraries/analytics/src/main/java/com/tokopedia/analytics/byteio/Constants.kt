package com.tokopedia.analytics.byteio

internal val TAG = "BYTEIO"

object Constants {
    const val EVENT_ORIGIN_FEATURE_KEY = "EVENT_ORIGIN_FEATURE"
    const val EVENT_ORIGIN_FEATURE_VALUE = "TEMAI"
}


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
    val isSingleSku: Boolean,
)

data class TrackStayProductDetail(
    val productId: String,
    val productCategory: String,
//    val entrance_info: TODO
    val productType: ProductType,
    val originalPrice: String,
    val salePrice: String,
    val isLoadData: Boolean,
    val isSingleSku: Boolean,
    val mainPhotoViewCount: Int,
    val skuPhotoViewCount: Int,
    val isSkuSelected: Boolean = true, // always selected in case of P1 flow
    val isAddCartSelected: Boolean,
)

data class TrackConfirmSku(
    val productId: String,
    val productCategory: String,
//    val entrance_info: TODO
    val productType: ProductType,
    val originalPrice: Double,
    val salePrice: Double,
    val skuId: String,
    val currency: String = "IDR",
    val isSingleSku: Boolean,
    val qty: String,
    val isHaveAddress: Boolean,
)

data class TrackConfirmCart(
    val productId: String,
    val productCategory: String,
//    val entrance_info: TODO
    val productType: ProductType,
    val originalPrice: Double,
    val salePrice: Double,
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
    val originalPrice: Double,
    val salePrice: Double,
    val buttonType: Int,
    val skuId: String,
    val currency: String = "IDR",
    val addSkuNum: Int,
    val skuNumBefore: Int = 0, // need development from BE
    val skuNumAfter: Int = 0, // need development from BE
    val isSuccess: Boolean,
    val failReason: String,
    val cartItemId: String,
)

data class SubmitOrderResult(
    val isSuccess: Boolean,
    val failReason: String,
    val shippingPrice: Double,
    val discountedShippingPrice: Double,
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
    const val SKU = "sku"
}

object QuitType {
    const val RETURN = "return"
    const val NEXT = "next"
    const val CLOSE = "close"
}

enum class EntranceForm(val str: String) {
    SEARCH_PURE_GOODS_CARD("search_pure_goods_card"),
    SEARCH_VIDEO_GOODS_CARD("search_video_goods_card"),
    SEARCH_SHOP_CARD_BIG("search_shop_card_big"),
    SEARCH_SHOP_CARD_SMALL("search_shop_card_small"),
    SEARCH_HORIZONTAL_GOODS_CARD("search_horizontal_goods_card"),
    PURE_GOODS_CARD("pure_goods_card"),
    DETAIL_GOODS_CARD("detail_goods_card"),
    HORIZONTAL_GOODS_CARD("horizontal_goods_card"),
    APPEND_GOODS_CARD("append_goods_card"),
}

enum class SourceModule(val str: String) {
    HOME_FOR_YOU("rec/ads/ops_homepage_outer_flow"),
    FOR_YOU_BEST_SELLER("rec/ads/ops_foru_bestseller_outer_flow"),
    FOR_YOU_TRENDING("rec/ads/ops_foru_trending_outer_flow"),
}

enum class SourcePageType(val str: String) {
    VIDEO("video"),
    LIVE("live"),
    PRODUCT_CARD("")
}

enum class EnterMethod(val str: String) {
    CLICK_TAB("click_tab"),
    SWITCH_TAB("switch_tab"),
    CLICK_BUBBLE("click_bubble"),
    CLICK_PUSH("click_push"),
    CLICK_WHATSAPP("click_whatsapp"),
    CLICK_INBOX("click_inbox"),
    CLICK_NO_ANCHOR_VIDEO_BUTTON("click_no_anchor_video_button"),
    CLICK_NO_ANCHOR_VIDEO_PIC("click_no_anchor_video_pic"),
    CLICK_VIEW_ORDER("click_view_order"),
    CLICK_PLACE_ORDER("click_place_order"),
    CLICK_VIDEO_GUIDE("click_video_guide"),
    CLICK_TT_TASK_PAGE_MALL_BROWSE_TASK("click_tt_task_page_mall_browse_task"),
    CLICK_TT_TASK_PAGE_COUPON_POPUP("click_tt_task_page_coupon_popup"),
    OTHER("other")
}

object EventName {
    const val PRODUCT_SHOW = "tiktokec_product_show"
    const val PRODUCT_CLICK = "tiktokec_product_click"
    const val ENTER_PAGE = "tiktokec_enter_page"
    const val CARD_SHOW = "tiktokec_card_show"
    const val CARD_CLICK = "tiktokec_card_click"
    const val ENTER_PRODUCT_DETAIL = "tiktokec_enter_product_detail"
    const val STAY_PRODUCT_DETAIL = "tiktokec_stay_product_detail"
    const val CONFIRM_SKU = "tiktokec_confirm_sku"
    const val CONFIRM_CART = "tiktokec_confirm_cart"
    const val CONFIRM_CART_RESULT = "tiktokec_confirm_cart_result"
    const val SUBMIT_ORDER_RESULT = "tiktokec_submit_order_result"

    // https://bytedance.sg.larkoffice.com/docx/MSiydFty1o0xIYxUe4LltuRHgue
    const val GLIDE_PAGE = "tiktokec_glide_page"
    const val REC_TRIGGER = "tiktokec_rec_trigger"
    const val SLIDE_BAR = "tiktokec_slide_bar"
}

object ActionType {
    const val GLIDE = "glide"
    const val CLICK_CARD = "click_card"
    const val SWITCH_TAB = "switch_tab"
}


object AppLogParam {
    const val ACTION_TYPE = "action_type"
    const val BAR_NAME = "bar_name"
    const val CARD_NAME = "card_name"
    const val ENTER_FROM = "enter_from"
    const val ENTRANCE_FORM = "entrance_form"
    const val GLIDE_DISTANCE = "glide_distance"
    const val GLIDE_TYPE = "glide_type"
    const val GROUP_ID = "group_id"
    const val IS_AD = "is_ad"
    const val IS_USE_CACHE = "is_use_cache"
    const val ITEM_ORDER = "item_order"
    const val LIST_NAME = "list_name"
    const val LIST_NUM = "list_num"
    const val MODULE_NAME = "module_name"
    const val PAGE_NAME = "page_name"
    const val PREVIOUS_PAGE = "previous_page"
    const val PRODUCT_ID = "product_id"
    const val REQUEST_ID = "request_id"
    const val REC_SESSION_ID = "req_session_id"
    const val SHOP_ID = "shop_id"
    const val SLIDE_TYPE = "slide_type"
    const val SOURCE_MODULE = "source_module"
    const val SOURCE_PAGE_TYPE = "source_page_type"
    const val TRACK_ID = "track_id"
}
