package com.tokopedia.play.broadcaster.ui.itemdecoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by jegul on 26/05/20
 */
class PlayEtalaseItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val offset4 = context.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val gridLayoutManager = parent.layoutManager as GridLayoutManager
        val spanCount = gridLayoutManager.spanCount

        if (position % spanCount != 0) { //right
            outRect.left = offset4
        } else {
            outRect.right = offset4
        }
    }
}