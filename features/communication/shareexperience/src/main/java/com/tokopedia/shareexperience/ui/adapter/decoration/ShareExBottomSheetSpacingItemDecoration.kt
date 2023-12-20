package com.tokopedia.shareexperience.ui.adapter.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shareexperience.ui.adapter.viewholder.ShareExLineSeparatorViewHolder

class ShareExBottomSheetSpacingItemDecoration(
    private val verticalSpacing: Int,
    private val horizontalSpacing: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount ?: 0
        val viewType = parent.adapter?.getItemViewType(parent.getChildAdapterPosition(view))

        /**
         * Exclude line separator from padding
         */
        if (viewType != ShareExLineSeparatorViewHolder.LAYOUT) {
            outRect.apply {
                when (position) {
                    0 -> {
                        top = verticalSpacing * 2
                        bottom = verticalSpacing
                    }
                    itemCount -> {
                        // Last Item
                        top = verticalSpacing
                        bottom = verticalSpacing * 2
                    }
                    else -> {
                        top = verticalSpacing
                        bottom = verticalSpacing
                    }
                }
                left = horizontalSpacing
                right = horizontalSpacing
            }
        }
    }
}

