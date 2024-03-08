package com.tokopedia.wishlist.detail.data.model

import com.tokopedia.wishlist.collection.data.model.WishlistCollectionTypeLayoutData

sealed class WishlistCollectionState {

    object InitialLoading : WishlistCollectionState()

    data class Set(
        val items: List<WishlistCollectionTypeLayoutData>,
        val shouldUpdateRecommendationScrollState: Boolean
    ) : WishlistCollectionState()

    data class Update(
        val items: List<WishlistCollectionTypeLayoutData>
    ) : WishlistCollectionState()

    data class Error(
        val throwable: Throwable
    ) : WishlistCollectionState()
}
