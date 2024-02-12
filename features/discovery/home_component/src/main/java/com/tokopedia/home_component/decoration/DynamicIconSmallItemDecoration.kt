package com.tokopedia.home_component.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.util.toDpInt

/**
 * Created by frenzel
 */
class DynamicIconSmallItemDecoration : RecyclerView.ItemDecoration() {
    companion object {
        private const val FIRST_POSITION_TOP = 0
        private const val FIRST_POSITION_BOTTOM = 1
        private val OUTSIDE_MARGIN = 16f.toDpInt()
        private val INNER_MARGIN_HORIZONTAL = 3f.toDpInt()
        private val INNER_MARGIN_VERTICAL = 2f.toDpInt()
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val pos = parent.getChildAdapterPosition(view)
        when (pos) {
            FIRST_POSITION_TOP, FIRST_POSITION_BOTTOM -> {
                outRect.left = OUTSIDE_MARGIN
                outRect.right = INNER_MARGIN_HORIZONTAL
            }
            //last position of card
            state.itemCount - 1, state.itemCount - 2 -> {
                outRect.right = OUTSIDE_MARGIN
                outRect.left = INNER_MARGIN_HORIZONTAL
            }
            //card between first and last
            else -> {
                outRect.right = INNER_MARGIN_HORIZONTAL
                outRect.left = INNER_MARGIN_HORIZONTAL
            }
        }

        if(pos.mod(2) == 0) {
            outRect.bottom = INNER_MARGIN_VERTICAL
            outRect.top = 0
        } else {
            outRect.bottom = 0
            outRect.top = INNER_MARGIN_VERTICAL
        }
    }
}
