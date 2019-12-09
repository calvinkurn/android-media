package com.tokopedia.productcard.options

internal interface Event {
    companion object {
        const val CLICK_WISHLIST = "clickWishlist"
    }
}

internal interface Action {
    companion object {
        const val MODULE = "module"
        const val LOGIN = "login"
        const val NON_LOGIN = "nonlogin"
        const val ADD_WISHLIST = "add wishlist"
        const val REMOVE_WISHLIST = "remove wishlist"
        const val CLICK_SIMILAR_BUTTON = "click - similar button"
    }
}

internal interface Label {
    companion object {
        const val KEYWORD_PRODUCT_ID = "keyword: %s - product id: %s"
        const val TOPADS = "topads"
        const val GENERAL = "general"
    }
}