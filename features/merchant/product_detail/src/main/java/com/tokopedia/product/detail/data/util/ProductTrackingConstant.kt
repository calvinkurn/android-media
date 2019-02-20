package com.tokopedia.product.detail.data.util

object ProductTrackingConstant {
    object Category {
        const val PDP = "Product Detail Page"
    }

    object Action {
        const val CLICK = "click"
        const val CLICK_CART_BUTTON_VARIANT = "click - cart button on sticky header"
        const val PRODUCT_CLICK = "productClick"
        const val TOPADS_CLICK = "click - top ads"
        const val TOPADS_IMPRESSION = "impression - top ads"
        const val CLICK_BY_ME = "click - by.me"
    }

    object Report {
        const val EVENT = "clickReport"
        const val EVENT_LABEL = "Report"
        const val NOT_LOGIN_EVENT_LABEL = "Report - Not Login"
    }

    object PDP {
        const val EVENT = "clickPDP"
    }

    object Affiliate {
        const val EVENT = "clickAffiliate"
        const val CATEGORY = "product detail page tokopedia by.me"
        const val ACTION = "click tambah ke by.me"
    }

    object ProductTalk {
        const val EVENT_LABEL = "Talk"
    }

    object ProductReview {
        const val EVENT_LABEL = "review"
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
}