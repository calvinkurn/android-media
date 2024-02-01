package com.tokopedia.wishlist.collection.view.adapter.itemdecoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.tokopedia.wishlist.collection.view.adapter.WishlistCollectionAdapter
import com.tokopedia.wishlist.collection.view.adapter.viewholder.WishlistCollectionItemViewHolder
import com.tokopedia.wishlist.collection.view.adapter.viewholder.WishlistCollectionLoaderItemViewHolder
import com.tokopedia.wishlist.collection.view.adapter.viewholder.WishlistCollectionRecommendationItemViewHolder
import com.tokopedia.wishlist.detail.util.WishlistConsts

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
        val viewPosition = parent.getChildAdapterPosition(view)
        val viewHolder = parent.getChildViewHolder(view)
        if (viewPosition > RecyclerView.NO_POSITION) {
            when (viewHolder) {
                is WishlistCollectionItemViewHolder,
                is WishlistCollectionLoaderItemViewHolder -> {
                    if (viewPosition % 2 == 0) {
                        outRect.set(0, 0, mItemOffset, 0)
                    } else {
                        outRect.set(mItemOffset, 0, 0, 0)
                    }
                }

                is WishlistCollectionRecommendationItemViewHolder -> {
                    val preRecommendationItemCount =
                        (parent.adapter as? WishlistCollectionAdapter)
                            ?.getItems()
                            ?.count { it.typeLayout != WishlistConsts.TYPE_RECOMMENDATION_LIST } ?: 0
                    if (preRecommendationItemCount % 2 == 0) {
                        if (viewPosition % 2 == 0) {
                            outRect.set(0, 0, mItemOffset, 0)
                        } else {
                            outRect.set(mItemOffset, 0, 0, 0)
                        }
                    } else {
                        if (viewPosition % 2 == 0) {
                            outRect.set(mItemOffset, 0, 0, 0)
                        } else {
                            outRect.set(0, 0, mItemOffset, 0)
                        }
                    }
                }

                else -> {
                    // no-op
                }
            }
        }
    }
}
