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
        val viewType = parent.adapter?.getItemViewType(parent.getChildAdapterPosition(view))

        /**
         * Exclude line separator from padding
         */
        if (viewType != ShareExLineSeparatorViewHolder.LAYOUT) {
            outRect.apply {
                top = verticalSpacing
                bottom = verticalSpacing
                left = horizontalSpacing
                right = horizontalSpacing
            }
        }
    }
}

