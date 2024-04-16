package com.tokopedia.home_component.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.util.toDpInt

class StaticMissionWidgetItemDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % SPAN_COUNT

        outRect.left = column * SPACING / SPAN_COUNT
        outRect.right = SPACING - (column + 1) * SPACING / SPAN_COUNT
    }

    companion object {
        private const val SPAN_COUNT = 4
        private val SPACING = 8f.toDpInt()
    }
}
