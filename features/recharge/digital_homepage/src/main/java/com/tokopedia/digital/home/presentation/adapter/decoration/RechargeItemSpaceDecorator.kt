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
    private val space: Int
): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildLayoutPosition(view)
        val lastPosition = parent.adapter?.itemCount?.minus(Int.ONE) ?: Int.ZERO
        if (position == Int.ZERO) {
            outRect.left = space
        }
        if (position == lastPosition) {
            outRect.right = space
        }
        outRect.bottom = space
    }
}
