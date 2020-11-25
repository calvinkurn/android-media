package com.tokopedia.play.broadcaster.ui.itemdecoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by jegul on 20/05/20
 */
class PlayFollowerItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val offset8 = context.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3) * -1

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildLayoutPosition(view)

        if (position > 0) outRect.left = offset8
        else super.getItemOffsets(outRect, view, parent, state)
    }
}