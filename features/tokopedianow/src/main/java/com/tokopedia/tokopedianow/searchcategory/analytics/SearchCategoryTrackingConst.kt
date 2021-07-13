package com.tokopedia.tokopedianow.searchcategory.analytics

object SearchCategoryTrackingConst {

    object Event {
        const val PRODUCT_VIEW = "productView"
        const val PRODUCT_CLICK = "productClick"
        const val ADD_TO_CART = "addToCart"
        const val PROMO_VIEW = "promoView"
        const val PROMO_CLICK = "promoClick"
    }

    object ECommerce {
        const val ECOMMERCE = "ecommerce"
        const val CURRENCYCODE = "currencyCode"
        const val IDR = "IDR"
        const val IMPRESSIONS = "impressions"
        const val CLICK = "click"
        const val ACTION_FIELD = "actionField"
        const val LIST = "list"
        const val PRODUCTS = "products"
        const val ADD = "add"
        const val PROMOTIONS = "promotions"
    }

    object Misc {
        const val NONE_OTHER = "none / other"
        const val TOKO_NOW = "toko now"
        const val USER_ID = "userId"
        const val HOME_AND_BROWSE = "home & browse"
        const val DEFAULT = "default"
    }
}