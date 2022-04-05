package com.tokopedia.home_component.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.util.toDpInt
import com.tokopedia.home_component.viewholders.CueWidgetCategoryViewHolder.Companion.CUE_WIDGET_TYPE_2x2

/**
 * Created by dhaba
 */
class CueWidgetCategoryItemDecoration(private val spacing: Int, private val gridType: Int) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view) // item position

        if (gridType == CUE_WIDGET_TYPE_2x2) {
            if (position % 2 == 0) {
                outRect.left = 16f.toDpInt()
                outRect.right = spacing / 2
            } else {
                outRect.left = spacing / 2
                outRect.right = 16f.toDpInt()
            }

            if (position == 0 || position == 1) {
                outRect.bottom = 6f.toDpInt()
            } else {
                outRect.top = 6f.toDpInt()
            }
        } else { // for grid type 3x2
            when {
                position % 3 == 0 -> {
                    outRect.left = 16f.toDpInt()
                }
                position % 3 == 1 -> {
                    outRect.left = spacing
                    outRect.right = spacing
                }
                else -> { // for right corner
                    outRect.right = 16f.toDpInt()
                }
            }
            if (position / 3 == 0) {
                outRect.bottom = 6f.toDpInt()
            } else {
                outRect.top = 6f.toDpInt()
            }
        }


    }
}