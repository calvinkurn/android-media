package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.balancewidget.atf2

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.util.toDpInt
import com.tokopedia.kotlin.extensions.view.ZERO

/**
 * Created by frenzel
 */
class BalanceSpacingItemDecoration : RecyclerView.ItemDecoration() {
    companion object {
        private const val FIRST_POSITION = 0
        private var OUTSIDE_MARGIN = 16f.toDpInt()
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        when (parent.getChildAdapterPosition(view)) {
            FIRST_POSITION -> {
                outRect.left = OUTSIDE_MARGIN
                outRect.right = Int.ZERO
            }
            //last position of card
            state.itemCount - 1 -> {
                outRect.right = OUTSIDE_MARGIN
                outRect.left = Int.ZERO
            }
            //card between first and last
            else -> {
                outRect.right = Int.ZERO
                outRect.left = Int.ZERO
            }
        }
    }
}
