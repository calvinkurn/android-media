package com.tokopedia.feedplus.view.analytics

/**
 * @author by astidhiyaa on 30/08/22
 */
object FeedTrackingEventLabel {
    const val PAGE_FEED = "Feed"
    const val PAGE_PRODUCT_LIST = "Product List"
    const val SCREEN_FEED_DETAIL = "/feed/product-detail"
    const val SCREEN_UNIFY_HOME_FEED = "/feed"

    object View {
        const val PRODUCTLIST_PDP = "Product List - PDP"
        const val PRODUCTLIST_SHOP = "Product List - Shop"
    }

    object Click {
        const val VISIT_SHOP = "Kunjungi Toko - Shop"
        const val ADD_TO_WISHLIST = "Add to Wishlist - "
        const val REMOVE_WISHLIST = "Remove from Wishlist - "
        const val TOP_ADS_PRODUCT = "TopAds Product"
        const val TOP_ADS_SHOP = "TopAds Shop"
        const val TOP_ADS_FAVORITE = "TopAds Favorite"
        const val FEED_BEFORE_LOGIN = "masuk sekarang"
    }
}