package com.tokopedia.play_common.ui.leaderboard.itemdecoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.play_common.R

/**
 * @author by astidhiyaa on 14/04/22
 */
class PlayLeaderBoardItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val dividerHeight = context.resources.getDimensionPixelOffset(R.dimen.play_leaderboard_winner_separator_height)

    private val mPaint = Paint().apply {
        color = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN300)
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)

        if (position != 0) { outRect.top = dividerHeight
        } else super.getItemOffsets(outRect, view, parent, state)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        for (index in 0 until parent.childCount) {
            val child = parent.getChildAt(index)

            if (index != 0) { c.drawRect(
                Rect(child.left, child.top - dividerHeight, parent.width, child.top), mPaint)
            }
        }
    }
}