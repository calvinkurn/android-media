package com.tokopedia.digital.home.presentation.adapter.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO

/**
 * created by @bayazidnasir on 31/10/2022
 */

class RechargeItemSpaceDecorator(
    space: Int
) : RecyclerView.ItemDecoration() {

    companion object {
        const val TWO_MULTIPLIER = 2
    }

    private val fullPadding = space
    private val halfPadding = space / TWO_MULTIPLIER
    private val verticalPadding = space / TWO_MULTIPLIER / TWO_MULTIPLIER

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildLayoutPosition(view)
        val lastPosition = parent.adapter?.itemCount?.minus(Int.ONE) ?: Int.ZERO
        if (position == Int.ZERO) {
            outRect.left = fullPadding * TWO_MULTIPLIER
            outRect.right = halfPadding
        } else if (position == lastPosition) {
            outRect.right = fullPadding * TWO_MULTIPLIER
            outRect.left = halfPadding
        } else {
            outRect.right = halfPadding
            outRect.left = halfPadding
        }
        outRect.top = verticalPadding
        outRect.bottom = verticalPadding
    }
}
