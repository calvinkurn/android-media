package com.tokopedia.play.broadcaster.ui.itemdecoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifyprinciples.R

class PlayBroadcastPreparationBannerItemDecoration(
    context: Context
) : RecyclerView.ItemDecoration() {

    companion object {
        private const val FIRST_ITEM = 0
    }

    private val spaceItemEdge = context.resources.getDimensionPixelOffset(R.dimen.spacing_lvl4)
    private val spaceItem = context.resources.getDimensionPixelOffset(R.dimen.spacing_lvl3)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val last = parent.adapter?.itemCount ?: 0

        when (position) {
            FIRST_ITEM -> outRect.left = spaceItemEdge
            last - 1 -> {
                outRect.left = spaceItem
                outRect.right = spaceItemEdge
            }
            else -> outRect.left = spaceItem
        }
    }

}
