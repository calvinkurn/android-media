package com.tokopedia.sellerhomecommon.presentation.view.viewhelper

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.orZero

/**
 * Created By @ilhamsuaib on 08/09/21
 */

class MilestoneMissionItemDecoration(private val margin: Int) : RecyclerView.ItemDecoration() {

    companion object {
        private const val FIRST_INDEX = 0
        private const val LAST_ONE = 1
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val lastIndex = parent.adapter?.itemCount.orZero().minus(LAST_ONE)
        val isFirstItem: Boolean = parent.getChildAdapterPosition(view) == FIRST_INDEX
        val isLastItem: Boolean = parent.getChildAdapterPosition(view) == lastIndex
        if (isFirstItem) {
            outRect.left = margin
        } else if (isLastItem) {
            outRect.right = margin
        }
    }
}