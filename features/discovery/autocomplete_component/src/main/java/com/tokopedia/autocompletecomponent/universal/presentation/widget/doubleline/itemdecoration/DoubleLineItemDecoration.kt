package com.tokopedia.autocompletecomponent.universal.presentation.widget.doubleline.itemdecoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifycomponents.toPx

class DoubleLineItemDecoration: RecyclerView.ItemDecoration() {

    companion object {
        private const val VERTICAL_GAP_DP = 12
        private const val HORIZONTAL_MARGIN = 16
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount

        val horizontalMargin = HORIZONTAL_MARGIN.toPx()
        val bottom = if (position == itemCount - 1) 0 else VERTICAL_GAP_DP.toPx()

        outRect.set(horizontalMargin, 0, horizontalMargin, bottom)
    }
}