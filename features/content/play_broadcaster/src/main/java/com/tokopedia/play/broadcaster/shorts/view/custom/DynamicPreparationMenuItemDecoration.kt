package com.tokopedia.play.broadcaster.shorts.view.custom

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.orZero

/**
 * Created By : Jonathan Darwin on November 09, 2022
 */
class DynamicPreparationMenuItemDecoration(context: Context): RecyclerView.ItemDecoration()  {

    private val offset24 = context.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl5)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildLayoutPosition(view)

        if(parent.adapter != null && position != parent.adapter?.itemCount.orZero()-1) outRect.bottom = offset24
        else super.getItemOffsets(outRect, view, parent, state)
    }
}
