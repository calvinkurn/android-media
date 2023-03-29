package com.tokopedia.tokopedianow.common.decoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.util.ViewUtil

class ChipListHorizontalDecoration(private val context: Context) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val margin = ViewUtil.getDpFromDimen(
            context = context,
            R.dimen.tokopedianow_chip_list_margin
        ).toInt()

        val spacing = ViewUtil.getDpFromDimen(
            context = context,
            R.dimen.tokopedianow_chip_list_spacing
        ).toInt()

        val currentPosition = parent.getChildAdapterPosition(view)
        val lastIndex = state.itemCount - 1
        val firstPosition = currentPosition == 0
        val lastPosition = currentPosition == lastIndex

        when {
            firstPosition -> {
                outRect.left = margin
                outRect.right = spacing
            }
            lastPosition -> {
                outRect.right = margin
            }
            else -> {
                outRect.right = spacing
            }
        }
    }
}
