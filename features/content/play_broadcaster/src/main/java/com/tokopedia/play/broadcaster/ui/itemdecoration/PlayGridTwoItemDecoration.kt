package com.tokopedia.play.broadcaster.ui.itemdecoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by jegul on 26/05/20
 */
class PlayGridTwoItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val offset4 = context.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2)
    private val offset16 = context.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val gridLayoutManager = parent.layoutManager as GridLayoutManager

        if (isNotFirstColumn(position, gridLayoutManager)) {
            outRect.left = offset4
        } else {
            outRect.right = offset4
        }

        outRect.bottom = offset16
    }

    private fun isNotFirstColumn(position: Int, gridLayoutManager: GridLayoutManager): Boolean {
        return position % gridLayoutManager.spanCount != 0
    }
}