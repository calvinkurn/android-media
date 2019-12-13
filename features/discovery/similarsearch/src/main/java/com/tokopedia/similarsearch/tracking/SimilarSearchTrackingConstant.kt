package com.tokopedia.similarsearch.tracking

internal const val ECOMMERCE = "ecommerce"

internal interface Event {
    companion object {
        const val PRODUCT_VIEW = "productView"
        const val PRODUCT_CLICK = "productClick"
        const val VIEW_SEARCH_RESULT = "viewSearchResult"
        const val CLICK_WISHLIST = "clickWishlist"
        const val CLICK_SIMILAR_SEARCH = "clickSimilarSearch"
    }
}

internal interface Category {
    companion object {
        const val SIMILAR_PRODUCT = "similar product"
    }
}

internal interface Action {
    companion object {
        const val IMPRESSION_PRODUCT = "impression - product"
        const val CLICK_SIMILAR_PRODUCT = "click - similar product"
        const val NO_SIMILAR_PRODUCT = "no similar product"
        const val ADD_WISHLIST = "add wishlist"
        const val REMOVE_WISHLIST = "remove wishlist"
        const val MODULE = "module"
        const val LOGIN = "login"
        const val NON_LOGIN = "nonlogin"
        const val CLICK_BUY_ON_SIMILAR_SEARCH = "click buy on similar search"
        const val CLICK_ADD_TO_CART_ON_SIMILAR_SEARCH = "click add to cart on similar search"
    }
}

internal interface Label {
    companion object {
        const val PRODUCT_ID = "product id: %s"
        const val ORIGIN_PRODUCT_ID_SCREEN_NAME = "origin product id: %s - %s"
        const val TOPADS = "topads"
        const val GENERAL = "general"
    }
}

internal interface ECommerce {
    
    companion object {
        const val CURRENCY_CODE = "currencyCode"
        const val IDR = "IDR"
        const val IMPRESSIONS = "impressions"
        const val NONE_OTHER = "none / other"
        const val CLICK = "click"
        const val ACTION_FIELD = "actionField"
        const val PRODUCTS = "products"
        const val ADD = "add"
    }
}