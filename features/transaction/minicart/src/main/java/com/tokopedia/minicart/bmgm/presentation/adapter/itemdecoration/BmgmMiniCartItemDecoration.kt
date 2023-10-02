package com.tokopedia.minicart.bmgm.presentation.adapter.itemdecoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.orZero

/**
 * Created by @ilhamsuaib on 05/08/23.
 */

class BmgmMiniCartItemDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        val index = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount.orZero()
        val isFirstItem = index == Int.ZERO
        val isLastItem = index == itemCount.minus(Int.ONE)

        when {
            isFirstItem -> setItemMargin(
                context = parent.context,
                outRect = outRect,
                left = 16,
                right = 4
            )

            isLastItem -> setItemMargin(
                context = parent.context,
                outRect = outRect,
                left = 4,
                right = 16
            )

            else -> setItemMargin(
                context = parent.context,
                outRect = outRect,
                left = 4,
                right = 4
            )
        }
    }

    private fun setItemMargin(context: Context, outRect: Rect, left: Int, right: Int) {
        outRect.left = context.dpToPx(left).toInt()
        outRect.right = context.dpToPx(right).toInt()
    }
}