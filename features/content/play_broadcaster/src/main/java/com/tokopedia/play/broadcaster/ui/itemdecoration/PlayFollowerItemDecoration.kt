package com.tokopedia.play.broadcaster.ui.itemdecoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by jegul on 20/05/20
 */
class PlayFollowerItemDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)

        if (position != 0) {
            val previousView = parent.getChildAt(position - 1)
            outRect.left = (previousView.width * -0.35f).toInt()
        } else super.getItemOffsets(outRect, view, parent, state)
    }
}