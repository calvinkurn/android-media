package com.tokopedia.media.picker.ui.adapter.decoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.R

class GridItemDecoration(
    context: Context,
    private val spanCount: Int
) : RecyclerView.ItemDecoration() {

    private var spacing = 0

    init {
        spacing = context.resources.getDimensionPixelSize(
            R.dimen.picker_item_padding
        )
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount

        // column * ((1f / spanCount) * spacing)
        outRect.left = column * spacing / spanCount

        // spacing - (column + 1) * ((1f /    spanCount) * spacing)
        outRect.right = spacing - (column + 1) * spacing / spanCount

        if (position >= spanCount) {
            outRect.top = spacing
        }
    }

}
