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

    object BADGE_URL {
        const val OFFICIAL_STORE_URL =  "https://ecs7.tokopedia.net/img/official_store_badge.png"
        const val POWER_MERCHANT_URL = "https://ecs7.tokopedia.net/img/power_merchant_badge.png"
    }

    object TOP_NAV_BUTTON {
        const val BACK_BUTTON = "Back Button"
        const val SEARCH_BAR = "Search Bar"
        const val SHARE = "Share"
        const val CART = "Cart"
        const val GLOBAL_MENU = "Global Menu"
    }

    object ProductTemplate {
        const val GRID =  "grid"
        const val LIST = "list"
    }

    object StaticFontColor{
        const val FONT_COLOR =  "#03AC0E"
    }

    object ProductCardModel{
        const val SALE_PRODUCT_STOCK = 100
        const val PRODUCT_STOCK = 0
        const val SOLD_PERCENTAGE_UPPER_LIMIT = 100
        const val SOLD_PERCENTAGE_LOWER_LIMIT = 0
        const val PDP_VIEW_THRESHOLD = 1000
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
    }

    object ChooseAddressGTMSSource{
        const val HOST_SOURCE = "discovery"
        const val HOST_TRACKING_SOURCE = "discovery page"
    }

    const val LABEL_FULFILLMENT = "fulfillment"
}