package com.tokopedia.similarsearch

internal const val ECOMMERCE = "ecommerce"

internal interface Event {
    companion object {
        const val PRODUCT_VIEW = "productView"
        const val PRODUCT_CLICK = "productClick"
        const val VIEW_SEARCH_RESULT = "viewSearchResult"
        const val CLICK_WISHLIST = "clickWishlist"
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
        const val EventNoSimilarProduct = "no similar product"
        const val ADD_WISHLIST = "add wishlist"
        const val REMOVE_WISHLIST = "remove wishlist"
        const val MODULE = "module"
        const val LOGIN = "login"
        const val NON_LOGIN = "nonlogin"
    }
}

internal interface Label {
    companion object {
        const val PRODUCT_ID = "product id: %s"
        const val TOPADS = "topads"
        const val GENERAL = "general"
    }
}