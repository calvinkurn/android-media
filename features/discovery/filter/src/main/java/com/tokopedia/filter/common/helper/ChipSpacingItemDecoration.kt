package com.tokopedia.filter.common.helper

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ChipSpacingItemDecoration(
        private val horizontalSpacing: Int,
        private val verticalSpacing: Int
): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.right = this.horizontalSpacing
        outRect.bottom = this.verticalSpacing
    }
}