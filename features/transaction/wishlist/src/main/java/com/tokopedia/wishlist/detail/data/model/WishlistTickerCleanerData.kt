package com.tokopedia.wishlist.detail.data.model

data class WishlistTickerCleanerData(
        val tickerCleanerData: WishlistUiModel.TickerState = WishlistUiModel.TickerState(),
        val bottomSheetCleanerData: WishlistUiModel.StorageCleanerBottomSheet = WishlistUiModel.StorageCleanerBottomSheet(),
        val countRemovableItems: Int = -1
)
