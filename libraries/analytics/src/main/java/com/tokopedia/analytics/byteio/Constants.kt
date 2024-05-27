package com.tokopedia.analytics.byteio

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.tokopedia.analytics.byteio.AppLogParam.PAGE_NAME
import com.tokopedia.analytics.byteio.pdp.AtcBuyType

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
    val productType: ProductType,
    val originalPrice: String,
    val salePrice: String,
    val isSingleSku: Boolean
)

data class TrackStayProductDetail(
    val productId: String,
    val productCategory: String,
    val productType: ProductType,
    val originalPrice: String,
    val salePrice: String,
    val isLoadData: Boolean,
    val isSingleSku: Boolean,
    val mainPhotoViewCount: Int,
    val skuPhotoViewCount: Int,
    var isSkuSelected: Boolean = true,
    val isAddCartSelected: Boolean
)

data class TrackConfirmSku(
    val productId: String,
    val productCategory: String,
    val productType: ProductType,
    val originalPrice: Double,
    val salePrice: Double,
    val skuId: String,
    val currency: String = "IDR",
    val isSingleSku: Boolean,
    val qty: String,
    val isHaveAddress: Boolean
)

data class TrackConfirmCart(
    val productId: String,
    val productCategory: String,
    val productType: ProductType,
    val originalPrice: Double,
    val salePrice: Double,
    val buttonType: String = "able_to_cart", // button can not be grayed out after atc clicked
    val skuId: String,
    val currency: String = "IDR",
    val addSkuNum: Int,
    val skuNumBefore: Int = 0, // need development from BE
    val skuNumAfter: Int = 0 // need development from BE
)

data class TrackConfirmCartResult(
    val productId: String,
    val productCategory: String,
    val productType: ProductType,
    val originalPrice: Double,
    val salePrice: Double,
    val buttonType: String = "able_to_cart", // button can not be grayed out after atc clicked
    val skuId: String,
    val currency: String = "IDR",
    val addSkuNum: Int,
    val skuNumBefore: Int = 0, // need development from BE
    val skuNumAfter: Int = 0, // need development from BE
    var isSuccess: Boolean? = null,
    var failReason: String = "",
    var cartItemId: String? = ""
)

data class SubmitOrderResult(
    val isSuccess: Boolean = true, // always success in ty page
    val failReason: String = "", // always empty, success in ty page
    val shippingPrice: Double,
    val discountedShippingPrice: Double,
    val totalPayment: Double,
    val discountedAmount: Double,
    val totalTax: Double = 0.0,
    val summaryInfo: String,
    val currency: String = "IDR",
    val deliveryInfo: String,
    val payType: String,
    val cartItemId: String,
    val skuId: String? = null,
    val orderId: String,
    val comboId: String,
    val productId: String
) {
    data class DeliveryInfo(
        @SerializedName("ship_from")
        val shipFrom: String = "local",
        @SerializedName("shipping_type")
        val shippingType: String,
        @SerializedName("eta")
        val eta: String
    ) {
        fun toJsonString(): String = Gson().toJson(this)
    }
}

data class CartClickAnalyticsModel(
    val buttonName: String = "cart_check_out",
    val cartItemId: String,
    val originalPriceValue: Double,
    val productId: String,
    val skuId: String,
    val skuNum: Int,
    val ItemCnt: Int,
    val salePriceValue: Double,
    val discountedAmount: Double
)

enum class ProductType(val type: Int) {
    AVAILABLE(1),
    SOLD_OUT(2),
    NOT_AVAILABLE(3),
    LIVE_REGION_NOT_AVAILABLE(4),
    NON_LIVE_REGION_NOT_AVAILABLE_OR_REMOVED(5)
}

object PageName {
    const val HOME = "homepage"
    const val FEED = "feed"
    const val OFFICIAL_STORE = "official_store"
    const val WISHLIST = "wishlist"
    const val UOH = "order_center"
    const val INBOX = "inbox"
    const val NOTIFICATION = "notification"
    const val PDP = "product_detail"
    const val SEARCH_RESULT = "search_result"
    const val CART = "cart"
    const val SHOP = "shop"
    const val SKU = "sku"
    const val ORDER_SUBMIT = "order_submit"
    const val EXTERNAL_PROMO = "external_promo"
    const val RECOMMENDATION = "recommendation"
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
    CONTENT_GOODS_CARD("content_goods_card"),
    HORIZONTAL_GOODS_CARD("horizontal_goods_card"),
    MISSION_HORIZONTAL_GOODS_CARD("mission_horizontal_goods_card"),
    TWO_MISSION_HORIZONTAL_GOODS_CARD("2mission_horizontal_goods_card"),
    APPEND_GOODS_CARD("append_goods_card")
}

object SourcePageType {
    const val VIDEO = "video"
    const val LIVE = "live"
    val PRODUCT_CARD
        get() = AppLogAnalytics.getCurrentData(PAGE_NAME)?.toString().orEmpty()
}

enum class EnterMethod(val str: String) {
    CLICK_EXTERNAL_ADS("click_external_ads"),
    CLICK_HOME_ICON("click_home_icon"),
    CLICK_APP_ICON("click_app_icon"),
    CLICK_RECOM_CARD_INFINITE("click_recom_card_infinite"),
    CLICK_INBOX_HOMEPAGE("click_inbox_homepage"),
    CLICK_NOTIFICATION_HOMEPAGE("click_notification_homepage"),
    CLICK_CART_ICON_HOMEPAGE("click_cart_icon_homepage"),
    CLICK_ATC_TOASTER_PDP("click_atc_toaster_pdp"),
    CLICK_WISHLIST_ICON("click_wishlist_icon"),
    CLICK_WISHLIST_ICONACCOUNT("click_wishlist_iconaccount")
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
    const val BUTTON_CLICK = "tiktokec_button_click"

    // https://bytedance.sg.larkoffice.com/docx/MSiydFty1o0xIYxUe4LltuRHgue
    const val GLIDE_PAGE = "tiktokec_glide_page"
    const val REC_TRIGGER = "tiktokec_rec_trigger"
    const val SLIDE_BAR = "tiktokec_slide_bar"
    const val CART_ENTRANCE_SHOW = "tiktokec_cart_entrance_show"
    const val CART_ENTRANCE_CLICK = "tiktokec_cart_entrance_click"

    //region https://bytedance.sg.larkoffice.com/sheets/YVaGsNyMfhqbjzt7HJvlH4FIgof
    const val PDP_BUTTON_SHOW = "tiktokec_button_show"
    const val PDP_BUTTON_CLICK = "tiktokec_button_click"
    const val PDP_BUTTON_CLICK_COMPLETED = "tiktokec_confirm_sku"
    //endregion
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

    // enter_from
    // Indicates where the page user is coming from
    // If from external (browser) and user enter pdp or discovery, this will be set to "external_promo"
    // Otherwise, this enter_from will be set automatically within activity lifecycle from page_name,
    //  as long as isWhitelisted is set to true.
    const val ENTER_FROM = "enter_from"

    const val ENTER_FROM_INFO = "enter_from_info" // supporting legacy param, only meant for getter
    const val ENTER_METHOD = "enter_method"
    const val ENTRANCE_INFO = "entrance_info"
    const val ENTRANCE_FORM = "entrance_form"
    const val GLIDE_DISTANCE = "glide_distance"
    const val DISTANCE_TO_TOP = "distance_to_top"
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
    const val REC_SESSION_ID = "rec_session_id"
    const val SHOP_ID = "shop_id"
    const val SLIDE_TYPE = "slide_type"
    const val SOURCE_MODULE = "source_module"
    const val SOURCE_PAGE_TYPE = "source_page_type"
    const val IS_ADDITIONAL = "is_additional"
    const val SOURCE_PREVIOUS_PAGE = "source_previous_page"
    const val TRACK_ID = "track_id"
    const val REC_PARAMS = "rec_params"
    const val AUTHOR_ID = "author_id"
    const val VOLUME = "volume"
    const val RATE = "rate"
    const val ORIGINAL_PRICE = "original_price"
    const val SALES_PRICE = "sales_price"
    const val MAIN_VIDEO_ID = "main_video_id"
    const val IS_SHADOW = "is_shadow"
    const val ACTIVITY_HASH_CODE = "activity_hash_code"
    const val ENTER_METHOD_DEFAULT_FORMAT = "click_%s_button"
    const val PARENT_PRODUCT_ID = "parent_product_id"
    const val PARENT_TRACK_ID = "parent_track_id"
    const val PARENT_REQUEST_ID = "parent_request_id"
    const val FIRST_TRACK_ID = "first_track_id"
    const val FIRST_SOURCE_PAGE = "first_source_page"
    const val CLICK_AREA = "click_area"
    val ENTER_METHOD_SEE_MORE
        get() = "${AppLogAnalytics.getCurrentData(PAGE_NAME)}_%s"
    const val IS_MAIN_PARENT = "is_main_parent_activity"
    const val SOURCE_CONTENT_ID = "source_content_id"
}

data class ButtonShowAnalyticData(
    val buttonName: String,
    val productId: String,
    val isSingleSku: Boolean,
    val buyType: AtcBuyType,
    val shopId: String
)

data class ButtonClickAnalyticData(
    val buttonName: String,
    val productId: String,
    val isSingleSku: Boolean,
    val buyType: AtcBuyType,
    val shopId: String
)

data class ButtonClickCompletedAnalyticData(
    val productId: String,
    val isSingleSku: Boolean,
    val skuId: String,
    val quantity: String,
    val productType: ProductType,
    val originalPrice: Double,
    val salePrice: Double,
    val followStatus: FollowStatus,
    val buyType: AtcBuyType,
    val cartId: String,
    val shopId: String
) {
    enum class FollowStatus(val value: Int) {
        UNFOLLOWED(0),
        FOLLOWED(3)
    }
}

enum class ClickAreaType(val value: String) {
    PRODUCT("product"),
    ATC("add_to_cart_button"),
    UNDEFINED("undefined")
}

enum class RefreshType(val value: Int) {
    UNKNOWN(-1),
    OPEN(0),
    REFRESH(1),
    LOAD_MORE(2),
    PUSH(3)
}
