package com.tokopedia.autocompletecomponent.universal.presentation.widget.listgrid.itemdecoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifycomponents.toPx

class ListGridItemDecoration: RecyclerView.ItemDecoration() {

    companion object {
        private const val VERTICAL_GAP_DP = 12
        private const val HORIZONTAL_GAP_DP = 12
        private const val MARGIN_LEFT_DP = 16
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)

        val left = if (position == 0 || position == 1) MARGIN_LEFT_DP.toPx() else 0
        val bottom = VERTICAL_GAP_DP.toPx()
        val right = HORIZONTAL_GAP_DP.toPx()

        outRect.set(left, 0, right, bottom)
    }
}