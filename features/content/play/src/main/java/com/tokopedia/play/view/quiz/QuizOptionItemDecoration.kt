package com.tokopedia.play.view.quiz

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * @author by astidhiyaa on 11/04/22
 */
class QuizOptionItemDecoration(context: Context) : RecyclerView.ItemDecoration() {
    private val topOffset =
        context.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        if (position != 0) {
            outRect.top = topOffset
        } else super.getItemOffsets(outRect, view, parent, state)
    }
}