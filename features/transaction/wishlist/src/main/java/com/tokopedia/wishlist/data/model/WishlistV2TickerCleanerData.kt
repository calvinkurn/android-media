package com.tokopedia.wishlist.data.model

import com.tokopedia.wishlist.data.model.response.WishlistV2Response

data class WishlistV2TickerCleanerData (
    val tickerCleanerData: WishlistV2UiModel.TickerState = WishlistV2UiModel.TickerState(),
    val bottomSheetCleanerData: WishlistV2UiModel.StorageCleanerBottomSheet = WishlistV2UiModel.StorageCleanerBottomSheet(),
    val countRemovableItems: Int = -1
)