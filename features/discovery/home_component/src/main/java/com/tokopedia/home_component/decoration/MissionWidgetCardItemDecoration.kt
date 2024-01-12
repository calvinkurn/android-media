package com.tokopedia.home_component.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.R
import com.tokopedia.home_component.util.toDpInt

/**
 * Created by dhaba
 */
class MissionWidgetCardItemDecoration : RecyclerView.ItemDecoration() {
    companion object {
        private const val FIRST_POSITION = 0
        private var INNER_MARGIN = 2f.toDpInt()
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val horizontalMargin = view.context.resources.getDimensionPixelSize(R.dimen.home_component_padding_horizontal_with_compat_padding)
        when (parent.getChildAdapterPosition(view)) {
            FIRST_POSITION -> {
                outRect.left = horizontalMargin
                outRect.right = INNER_MARGIN
            }
            //last position of card
            state.itemCount - 1 -> {
                outRect.right = horizontalMargin
                outRect.left = INNER_MARGIN
            }
            //card between first and last
            else -> {
                outRect.right = INNER_MARGIN
                outRect.left = INNER_MARGIN
            }
        }
        outRect.bottom = view.context.resources.getDimensionPixelSize(R.dimen.home_component_padding_bottom_with_compat_padding_translated)
    }
}
