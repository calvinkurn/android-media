package com.tokopedia.home_component.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.util.toDpInt

/**
 * Created by frenzel
 */
class DynamicIconBigItemDecoration : RecyclerView.ItemDecoration() {
    companion object {
        private const val FIRST_POSITION = 0
        private val OUTSIDE_MARGIN = 8f.toDpInt()
        private val INNER_MARGIN = 0f.toDpInt()
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
                outRect.right = INNER_MARGIN
            }
            //last position of card
            state.itemCount - 1 -> {
                outRect.right = OUTSIDE_MARGIN
                outRect.left = INNER_MARGIN
            }
            //card between first and last
            else -> {
                outRect.right = INNER_MARGIN
                outRect.left = INNER_MARGIN
            }
        }
    }
}
