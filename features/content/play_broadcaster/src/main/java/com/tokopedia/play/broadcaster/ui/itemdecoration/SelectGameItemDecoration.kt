package com.tokopedia.play.broadcaster.ui.itemdecoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created By : Jonathan Darwin on March 29, 2022
 */
class SelectGameItemDecoration(context: Context): RecyclerView.ItemDecoration()  {

    private val offset8 = context.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildLayoutPosition(view)

        outRect.apply {
            if(position % 2 == 0) right = offset8
            else left = offset8

            bottom = offset8
        }
    }
}