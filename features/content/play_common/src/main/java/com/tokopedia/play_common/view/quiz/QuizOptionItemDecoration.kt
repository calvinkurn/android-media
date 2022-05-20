package com.tokopedia.play_common.view.quiz

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play_common.R

/**
 * @author by astidhiyaa on 11/04/22
 */
class QuizOptionItemDecoration(context: Context) : RecyclerView.ItemDecoration() {
    private val topOffset =
        context.resources.getDimensionPixelOffset(R.dimen.play_dp_12)

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