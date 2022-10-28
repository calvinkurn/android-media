package com.tokopedia.play.broadcaster.ui.itemdecoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.orZero

/**
 * Created By : Jonathan Darwin on April 04, 2022
 */
class QuizOptionItemDecoration(context: Context): RecyclerView.ItemDecoration()  {

    private val offset16 = context.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildLayoutPosition(view)

        if(parent.adapter != null && position != parent.adapter?.itemCount.orZero()-1) outRect.bottom = offset16
        else super.getItemOffsets(outRect, view, parent, state)
    }
}