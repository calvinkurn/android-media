package com.tokopedia.wishlistcollection.view.adapter.itemdecoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class WishlistCollectionItemOffsetDecoration(private val mItemOffset: Int) : ItemDecoration() {
    constructor(
        @NonNull context: Context,
        @DimenRes itemOffsetId: Int
    ) : this(context.resources.getDimensionPixelSize(itemOffsetId))

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        if (position % 2 == 0) {
            outRect.set(0, 0, mItemOffset, 0)
        } else {
            outRect.set(mItemOffset, 0, 0, 0)
        }
    }
}
