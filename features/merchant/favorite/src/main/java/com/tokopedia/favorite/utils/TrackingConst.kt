package com.tokopedia.favorite.utils

interface TrackingConst {
    interface Event {
        companion object {
            const val FAVORITE = "clickFavorite"
        }
    }

    interface Category {
        companion object {
            const val HOMEPAGE = "Homepage"
        }
    }

    interface Action {
        companion object {
            const val CLICK_SHOP_FAVORITE = "favorite - click shop"
        }
    }
}
