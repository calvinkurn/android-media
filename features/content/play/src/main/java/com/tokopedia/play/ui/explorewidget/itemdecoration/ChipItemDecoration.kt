package com.tokopedia.play.ui.explorewidget.itemdecoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifyprinciples.R as unifyR

/**
 * @author by astidhiyaa on 06/12/22
 */
class ChipItemDecoration(context: Context) : RecyclerView.ItemDecoration() {
    private val offset4 by lazy(LazyThreadSafetyMode.NONE) {
        context.resources.getDimensionPixelOffset(unifyR.dimen.unify_space_4)
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        if (position != 0) outRect.left = offset4
        else super.getItemOffsets(outRect, view, parent, state)
    }
}
