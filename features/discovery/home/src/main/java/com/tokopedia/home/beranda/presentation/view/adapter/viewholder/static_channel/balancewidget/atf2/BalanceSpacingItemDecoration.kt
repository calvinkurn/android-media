package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.balancewidget.atf2

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ZERO

/**
 * Created by frenzel
 */
class BalanceSpacingItemDecoration : RecyclerView.ItemDecoration() {
    companion object {
        private const val FIRST_POSITION = 0
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val margin = view.context.resources.getDimensionPixelSize(com.tokopedia.home.R.dimen.balance_atf2_outer_margin)
        when (parent.getChildAdapterPosition(view)) {
            FIRST_POSITION -> {
                outRect.left = margin
                outRect.right = Int.ZERO
            }
            // last position of card
            state.itemCount - 1 -> {
                outRect.right = margin
                outRect.left = Int.ZERO
            }
            // card between first and last
            else -> {
                outRect.right = Int.ZERO
                outRect.left = Int.ZERO
            }
        }
    }
}
