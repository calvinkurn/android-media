package com.tokopedia.home_component.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.util.toDpInt
import com.tokopedia.home_component.R as home_componentR

/**
 * Created by frenzel
 */
class MissionWidgetClearItemDecoration : RecyclerView.ItemDecoration() {
    companion object {
        private const val FIRST_POSITION = 0
        private val INNER_MARGIN = 0f.toDpInt()
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val horizontalMargin = view.context.resources.getDimensionPixelSize(home_componentR.dimen.home_mission_widget_clear_horizontal_padding)
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
        outRect.bottom = view.context.resources.getDimensionPixelSize(home_componentR.dimen.home_mission_widget_clear_bottom_padding)
    }
}
