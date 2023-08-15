package com.tokopedia.discovery2

object Constant {

    object ClaimCouponConstant {
        const val KLAIM = "Klaim"
        const val DIKLAIM = "Diklaim"
        const val HABIS = "Habis"
        const val NOT_LOGGEDIN = "Not Logged In"
        const val DOUBLE_COLUMNS = "double"
    }

    object BrandRecommendation {
        const val SQUARE_DESIGN = "v1"
        const val RECTANGLE_DESIGN = "v2"
    }

    object MultipleShopMVCCarousel{
        const val SINGLE_ITEM_DESIGN = "s1"
        const val CAROUSEL_ITEM_DESIGN = "s2"
    }

    object TOP_NAV_BUTTON {
        const val BACK_BUTTON = "Back Button"
        const val SEARCH_BAR = "Search Bar"
        const val SHARE = "Share"
        const val CART = "Cart"
        const val GLOBAL_MENU = "Global Menu"
        const val INBOX = "Inbox"
        const val NOTIF = "Notif"
    }

    object ProductTemplate {
        const val GRID =  "grid"
        const val LIST = "list"
    }

    object ATCButtonCTATypes {
        const val MINI_CART = "minicart_atc"
        const val GENERAL_CART = "general_atc"
    }

    object ProductCardModel{
        const val SALE_PRODUCT_STOCK = 100
        const val PRODUCT_STOCK = 0
        const val SOLD_PERCENTAGE_UPPER_LIMIT = 100
        const val SOLD_PERCENTAGE_LOWER_LIMIT = 0
        const val PDP_VIEW_THRESHOLD = 1000
    }

    object Dimensions{
        const val HEIGHT = "height"
        const val WIDTH = "width"
    }

    object EmptyStateTexts{
        const val TITLE = "Awas keduluan pembeli lain!"
        const val DESCRIPTION = "Aktifkan pengingat supaya kamu nggak ketinggalan penawaran seru dari seller-seller Tokopedia!"
        const val EMPTY_IMAGE = "ic_product_empty_state.png"

        const val FILTER_TITLE = "Oops, barangnya nggak ketemu"
        const val FILTER_DESCRIPTION = "Coba kurangi filter yang sedang aktif, atau reset filter buat lanjutkan penjelajahanmu!"
        const val FILTER_BUTTON_TEXT = "Reset Filter"
        const val FILTER_EMPTY_IMAGE = "filtered_product_empty_state.png"
    }

    object ChooseAddressQueryParams{
        const val RPC_USER_ADDRESS_ID = "rpc_UserAddressId"
        const val RPC_USER_CITY_ID = "rpc_UserCityId"
        const val RPC_USER_DISTRICT_ID = "rpc_UserDistrictId"
        const val RPC_USER_LAT = "rpc_UserLat"
        const val RPC_USER_LONG = "rpc_UserLong"
        const val RPC_USER_POST_CODE = "rpc_UserPostCode"
        const val RPC_USER_WAREHOUSE_ID = "rpc_UserWarehouseId"
        const val RPC_USER_WAREHOUSE_IDS = "rpc_UserWarehouseIds"
        const val RPC_PRODUCT_ID = "rpc_ProductId"
        const val USER_ADDRESS_KEY = "user_address"
    }

    object ChooseAddressGTMSSource{
        const val HOST_SOURCE = "discovery"
        const val HOST_TRACKING_SOURCE = "discovery page"

        const val CATEGORY_HOST_SOURCE = "category"
        const val CATEGORY_HOST_TRACKING_SOURCE = "category page"
    }

    object Calendar{
        const val CAROUSEL = "carousel"
        const val DYNAMIC = "dynamic"
        const val STATIC = "static"
        const val SINGLE = "single"
        const val DOUBLE = "double"
        const val GRID = "grid"
        const val TRIPLE = "triple"
    }

    object QueryParamConstants{
        const val RPC_DYNAMIC_SUBTITLE = "rpc_DynamicSubtitle"
        const val RPC_TARGET_TITLE_ID = "rpc_TargetTitleId"
        const val QUERY_PARAMS_KEY = "query_params_key"
    }

    const val LABEL_FULFILLMENT = "fulfillment"
    const val DISCO_EMPTY_STATE_IMG = "disco_empty_state_img"
    const val DISCO_PAGE_SOURCE = "discovery_page_source"
    const val DISCOVERY_APPLINK = "discovery_applink"

    object DiscoveryPageSource {
        const val HOME = "home"
    }

    object TopAdsSdk{
        const val TOP_ADS_GSLP_TDN = "topads gslp"
    }

    const val RESETTING_SELECTED_TAB = -1

    object ProductHighlight{
        const val SINGLE = "single"
        const val DOUBLE = "double"
        const val TRIPLE = "triple"
        const val DOUBLESINGLEEMPTY = "doubleSingleEmpty"
        const val TRIPLESINGLEEMPTY = "tripleSingleEmpty"
        const val TRIPLEDOUBLEEMPTY = "tripleDoubleEmpty"
        const val STATUS = "status"
        const val PROMO = "promo"
    }

    const val REDIRECTION = "redirection"
    const val NAVIGATION = "navigation"
}
