package com.tokopedia.content.product.preview.view.components.items

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.content.product.preview.R as contentproductpreviewR

class ProductThumbnailItemDecoration(
    context: Context
) : RecyclerView.ItemDecoration() {

    private val spaceItemEdge = context.resources.getDimensionPixelOffset(contentproductpreviewR.dimen.product_preview_space_16)
    private val spaceItem = context.resources.getDimensionPixelOffset(contentproductpreviewR.dimen.product_preview_space_6)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val last = parent.adapter?.itemCount ?: FIRST_ITEM

        when (position) {
            FIRST_ITEM -> outRect.left = spaceItemEdge
            last - 1 -> {
                outRect.left = spaceItem
                outRect.right = spaceItemEdge
            }
            else -> outRect.left = spaceItem
        }
    }

    companion object {
        private const val FIRST_ITEM = 0
    }

}
