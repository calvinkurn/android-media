package com.tokopedia.wishlist.data.model

import com.tokopedia.wishlist.data.model.response.WishlistV2Response

data class WishlistV2TickerCleanerData (
    val tickerCleanerData: WishlistV2Response.Data.WishlistV2.TickerState = WishlistV2Response.Data.WishlistV2.TickerState(),
    val bottomSheetCleanerData: WishlistV2Response.Data.WishlistV2.StorageCleanerBottomSheet = WishlistV2Response.Data.WishlistV2.StorageCleanerBottomSheet(),
    val countRemovableItems: Int = -1
)