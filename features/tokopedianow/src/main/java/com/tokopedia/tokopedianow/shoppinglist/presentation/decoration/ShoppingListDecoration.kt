package com.tokopedia.tokopedianow.shoppinglist.presentation.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.HorizontalProductCardItemViewHolder
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class ShoppingListDecoration : RecyclerView.ItemDecoration() {
    companion object {
        const val INVALID_VIEW_TYPE = -1
        const val ADAPTER_START_INDEX = 0
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val absolutePos = parent.getChildAdapterPosition(view)
        if (isProductItem(parent, absolutePos)) outRect.bottom = view.context.resources.getDimensionPixelSize(unifyprinciplesR.dimen.spacing_lvl4)
    }

    private fun isProductItem(parent: RecyclerView, viewPosition: Int): Boolean = HorizontalProductCardItemViewHolder.LAYOUT == getRecyclerViewViewType(parent, viewPosition)

    private fun getRecyclerViewViewType(parent: RecyclerView, viewPosition: Int): Int {
        val adapter = parent.adapter ?: return INVALID_VIEW_TYPE
        val isInvalidPosition = viewPosition !in ADAPTER_START_INDEX until adapter.itemCount
        return if (isInvalidPosition) INVALID_VIEW_TYPE else adapter.getItemViewType(viewPosition)
    }
}
