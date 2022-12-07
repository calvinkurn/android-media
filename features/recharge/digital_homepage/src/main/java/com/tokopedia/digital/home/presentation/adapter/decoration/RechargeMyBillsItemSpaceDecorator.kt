package com.tokopedia.digital.home.presentation.adapter.decoration

import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.dpToPx

/**
 * created by @bayazidnasir on 31/10/2022
 */

class RechargeMyBillsItemSpaceDecorator(displayMetrics: DisplayMetrics): RecyclerView.ItemDecoration() {

    companion object {
        const val OUTER_SIDE_PADDING = 14
        const val INNER_SIDE_PADDING = 2
    }

    private val outerSidePadding = OUTER_SIDE_PADDING.dpToPx(displayMetrics)
    private val innerSidePadding = INNER_SIDE_PADDING.dpToPx(displayMetrics)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildLayoutPosition(view)
        val lastPosition = parent.adapter?.itemCount?.minus(Int.ONE) ?: Int.ZERO
        if (position == Int.ZERO) {
            outRect.left = outerSidePadding
            outRect.right = innerSidePadding
        } else if (position == lastPosition) {
            outRect.right = outerSidePadding
            outRect.left = innerSidePadding
        } else {
            outRect.right = innerSidePadding
            outRect.left = innerSidePadding
        }
        outRect.top = Int.ZERO
        outRect.bottom = Int.ZERO
    }
}
