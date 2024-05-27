package com.tokopedia.home_component.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.util.toDpInt

class StaticMissionWidgetItemDecoration(
    private val spanCount: Int,
    private val spacingSize: Int = 8f.toDpInt()
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount

        outRect.left = column * spacingSize / spanCount
        outRect.right = spacingSize - (column + 1) * spacingSize / spanCount
    }

    companion object {
        private const val SPAN_2 = 2
        private const val SPAN_4 = 4

        fun span2() = StaticMissionWidgetItemDecoration(SPAN_2)
        fun span4() = StaticMissionWidgetItemDecoration(SPAN_4)
    }
}
