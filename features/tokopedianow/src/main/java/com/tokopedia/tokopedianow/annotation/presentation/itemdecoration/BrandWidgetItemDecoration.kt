package com.tokopedia.tokopedianow.annotation.presentation.itemdecoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.orZero

class BrandWidgetItemDecoration: ItemDecoration() {

    companion object {
        private const val FIRST_ITEM_INDEX = 0

        private const val ITEM_SPACING = 8
        private const val WIDGET_SPACING = 16
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount.orZero() - 1

        val isFirstItem = position == FIRST_ITEM_INDEX
        val isLastItem = position == itemCount

        val itemSpacing = view.context.dpToPx(ITEM_SPACING).toInt()
        val widgetSpacing = view.context.dpToPx(WIDGET_SPACING).toInt()

        if (isFirstItem) {
            outRect.left = widgetSpacing
            outRect.right = itemSpacing
        } else if (isLastItem) {
            outRect.right = widgetSpacing
        } else {
            outRect.right = itemSpacing
        }
    }
}
