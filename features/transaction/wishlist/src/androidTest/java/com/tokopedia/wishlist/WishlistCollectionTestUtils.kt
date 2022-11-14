package com.tokopedia.wishlist

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.wishlistcollection.view.adapter.WishlistCollectionAdapter

internal fun RecyclerView?.getWishlistItemAdapter(): WishlistCollectionAdapter {
    val wishlistCollectionAdapter = this?.adapter as? WishlistCollectionAdapter

    if (wishlistCollectionAdapter == null) {
        val detailMessage = "Adapter is not ${WishlistCollectionAdapter::class.java.simpleName}"
        throw AssertionError(detailMessage)
    }

    return wishlistCollectionAdapter
}
