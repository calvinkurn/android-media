package com.tokopedia.autocompletecomponent.universal.presentation.itemdecoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifycomponents.toPx
import androidx.recyclerview.widget.DividerItemDecoration

class UniversalSearchItemDecoration(
    context: Context,
    orientation: Int,
): DividerItemDecoration(
    context,
    orientation
) {
    companion object {
        private const val DIVIDER_HEIGHT_DP = 8
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        outRect.set(0, 0, 0, DIVIDER_HEIGHT_DP.toPx())
    }
}