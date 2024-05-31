package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.balance

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.home.R as homeR

/**
 * Created by frenzel
 */
class BalanceSpacingItemDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val innerMargin = view.context.resources.getDimensionPixelSize(homeR.dimen.balance_widget_inner_margin)
        val outerMargin = view.context.resources.getDimensionPixelSize(homeR.dimen.balance_widget_outer_margin)
        when (parent.getChildAdapterPosition(view)) {
            0 -> {
                outRect.left = outerMargin
                outRect.right = innerMargin
            }
            state.itemCount - 1 -> {
                outRect.right = outerMargin
                outRect.left = innerMargin
            }
            else -> {
                outRect.right = innerMargin
                outRect.left = innerMargin
            }
        }
    }
}
