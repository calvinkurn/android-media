package com.tokopedia.shareexperience.ui.adapter.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ShareExVerticalSpacingItemDecoration(
    private val spacing: Int,
    private val doubleTopItemSpacing: Boolean,
    private val doubleBottomItemSpacing: Boolean
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount ?: 0

        outRect.apply {
            when (position) {
                0 -> {
                    top = if (doubleTopItemSpacing) {
                        spacing * 2
                    } else {
                        spacing
                    }
                    bottom = spacing
                }
                itemCount -> {
                    // Last Item
                    top = spacing
                    bottom = if (doubleBottomItemSpacing) {
                        spacing * 2
                    } else {
                        spacing
                    }
                }
                else -> {
                    top = spacing
                    bottom = spacing
                }
            }
        }
    }
}

