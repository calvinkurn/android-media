package com.tokopedia.product.detail.data.util

object ProductTrackingConstant {
    object Category {
        const val PDP = "Product Detail Page"
        const val PRODUCT_PAGE = "Product Page"
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
        const val EVENT_CLICK_PDP = "clickPDP"
    }

    object Wishlist{
        const val EVENT = "clickWishlist"
        const val CATEGORY = "Wishlist"
        const val LABEL = "Add To Wishlist"
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
}