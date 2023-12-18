package com.tokopedia.wishlist.collection.util

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener

abstract class WishlistEndlessScrollListener(
    layoutManager: RecyclerView.LayoutManager
) : EndlessRecyclerViewScrollListener(layoutManager) {

    fun setLoading(isLoading: Boolean) {
        this.loading = isLoading
    }
}
