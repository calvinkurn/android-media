package com.tokopedia.product.detail.data.util

object ProductTrackingConstant {
    const val USER_NON_LOGIN = "non login"

    object Category {
        const val PDP = "product detail page"
        const val PRODUCT_PAGE = "Product Page"
        const val PDP_AFTER_ATC = "product detail page after atc"
        const val TOP_NAV_SEARCH_PDP = "top nav - search - product detail page"
        const val TOP_NAV_SHARE_PDP = "top nav - product detail page"
    }

    object Tracking{
        const val KEY_EVENT = "event"
        const val KEY_CATEGORY = "eventCategory"
        const val KEY_ACTION = "eventAction"
        const val KEY_LABEL = "eventLabel"
        const val KEY_ECOMMERCE = "ecommerce"
        const val KEY_PRODUCT_PROMO = "promoClick"
        const val KEY_PROMOTIONS = "promotions"
        const val KEY_USER_ID = "user_id"
        const val KEY_DETAIl = "detail"

        const val PRODUCT_DETAIL_SCREEN_NAME = "/product"

        const val ID = "id"
        const val NAME = "name"
        const val PROMO_POSITION = "position"
        const val PROMO_ID = "promo_id"
        const val PROMO_CODE = "promo_id"

        const val ACTION_FIELD = "actionField"
        const val LIST = "list"
        const val PRODUCTS = "products"
        const val IMPRESSIONS = "impressions"
        const val PRICE = "price"
        const val BRAND = "brand"
        const val DEFAULT_VALUE = "none / other"
        const val VARIANT = "variant"
        const val CATEGORY = "category"
        const val LIST_DEFAULT = "/product - "
        const val LIST_RECOMMENDATION = " - rekomendasi untuk anda - "
        const val LIST_PRODUCT_AFTER_ATC = "/productafteratc  - "
        const val CURRENCY_CODE = "currencyCode"
        const val CURRENCY_DEFAULT_VALUE = "IDR"
        const val VALUE_BEBAS_ONGKIR = "bebas ongkir"
        const val VALUE_NONE_OTHER = "none / other"
        const val KEY_PRODUCT_ID = "productId"

        const val KEY_DIMENSION_81 = "dimension81"
        const val KEY_DIMENSION_83 = "dimension83"
        const val KEY_DIMENSION_54 = "dimension54"
        const val KEY_DIMENSION_55 = "dimension55"
        const val KEY_DIMENSION_38 = "dimension38"
    }

    object Action {
        const val CLICK = "click"
        const val CLICK_CART_BUTTON_VARIANT = "click - cart button on sticky header"
        const val PRODUCT_CLICK = "productClick"
        const val PRODUCT_VIEW = "productView"
        const val RECOMMENDATION_CLICK = "clickRecommendation"
        const val TOPADS_CLICK = "click - product recommendation"
        const val TOPADS_IMPRESSION = "impression - product recommendation"
        const val CLICK_BY_ME = "click - by.me"
        const val CLICK_SHIPPING = "click - shipping"
        const val CLICK_PRODUCT_PICTURE = "click - product picture"
        const val SWIPE_PRODUCT_PICTURE = "click - swipe product picture"
        const val CLICK_SHIPPING_RATE_ESTIMATION = "click - shipping rate estimation"
        const val ACTION_WISHLIST_ON_PRODUCT_RECOMMENDATION = " - wishlist on product recommendation"
        const val CLICK_APPLY_LEASING = "click - ajukan kredit"
        const val VIEW_HELP_POP_UP_WHEN_ATC = "view help pop up when atc"
        const val CLICK_REPORT_ON_HELP_POP_UP_ATC = "click report on help pop up atc"
        const val CLICK_CLOSE_ON_HELP_POP_UP_ATC = "click close on help pop up atc"
        const val CLICK_SEARCH_BOX = "click search box"
        const val CLICK_RIBBON_TRADE_IN = "click - ribbon trade in"
        const val CLICK_SEE_MORE_WIDGET = "click - see more on widget %s"
        const val CLICK_SHARE_PDP = "click - share button"
        const val CLICK_READ_MORE = "click - baca selengkapnya"
        const val CLICK_TAB_DESCRIPTION_ON_PRODUCT_DESCRIPTION = "click - tab deskripsi on description area"
        const val CLICK_TAB_SPECIFICATION_ON_PRODUCT_DESCRIPTION = "click - tab spesifikasi on description area"
        const val CLICK_LIHAT_SEMUA_ON_SIMULASI_CICILAN = "click - lihat semua metode on simulasi cicilan widget"



    }

    object Label {
        const val EMPTY_LABEL = ""
        const val CLICK = "click"
        const val PDP = "pdp"
    }

    object Report {
        const val EVENT = "clickReport"
        const val EVENT_LABEL = "Report"
        const val NOT_LOGIN_EVENT_LABEL = "Report - Not Login"
    }

    object PDP {
        const val EVENT_CLICK_PDP = "clickPDP"
        const val EVENT_VIEW_PDP = "viewPDP"
        const val EVENT_CLICK_TOP_NAV = "clickTopNav"
    }

    object Affiliate {
        const val CLICK_AFFILIATE = "clickAffiliate"
        const val CATEGORY = "product detail page tokopedia by.me"
        const val ACTION = "click tambah ke by.me"
        const val ACTION_CLICK_WISHLIST = "click wishlist"
    }

    object ProductTalk {
        const val TALK = "Talk"
    }

    object ProductReview {
        const val REVIEW = "review"
    }

    object ImageReview {
        const val ACTION_SEE_ITEM = "click - review gallery on foto dari pembeli"
        const val ACTION_SEE_ALL = "click - lihat semua review gallery"
    }

    object MerchantVoucher {
        const val EVENT = "promoClick"
        const val ACTION = "promo banner"
        const val MERCHANT_VOUCHER = "merchant voucher"
        const val DETAIL = "mvc detail"
        const val SEE_ALL = "see all"
    }

    object TopAds {
        const val PDP_TOPADS = "/productdetail - top ads'"
    }

    object Message {
        const val EVENT = "clickMessageShop"
        const val LABEL = "Message Shop"
    }

    object PageNameRecommendation {
        const val PDP_1 = "pdp_1"
        const val PDP_2 = "pdp_2"
        const val PDP_3 = "pdp_3"
        const val PDP_4 = "pdp_4"
    }
}