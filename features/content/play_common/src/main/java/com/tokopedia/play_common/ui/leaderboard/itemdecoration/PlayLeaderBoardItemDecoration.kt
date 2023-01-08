package com.tokopedia.play_common.ui.leaderboard.itemdecoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.play_common.R
import com.tokopedia.play_common.ui.leaderboard.viewholder.PlayGameViewHolder
import com.tokopedia.unifyprinciples.R as unifyR

/**
 * @author by astidhiyaa on 14/04/22
 */
class PlayLeaderBoardItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val dividerHeight =
        context.resources.getDimensionPixelOffset(R.dimen.play_leaderboard_winner_separator_height)
    private val startOffset = context.resources.getDimensionPixelOffset(unifyR.dimen.spacing_lvl6)
    private val topOffset by lazy(LazyThreadSafetyMode.NONE) {
        context.resources.getDimensionPixelOffset(R.dimen.play_dp_12)
    }
    private val defaultOffset by lazy(LazyThreadSafetyMode.NONE) {
        context.resources.getDimensionPixelOffset(unifyR.dimen.unify_space_8)
    }

    private val mPaint = Paint().apply {
        color = MethodChecker.getColor(context, unifyR.color.Unify_NN300)
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val index = parent.getChildAdapterPosition(view)
        when (parent.findViewHolderForAdapterPosition(index)) {
            is PlayGameViewHolder.Quiz -> {
                outRect.top = topOffset
                outRect.left = defaultOffset
                outRect.right = defaultOffset
            }
            is PlayGameViewHolder.Winner -> outRect.top = topOffset
            is PlayGameViewHolder.Header -> {
                outRect.top = defaultOffset
                outRect.left = defaultOffset
                outRect.right = defaultOffset
            }
            else -> super.getItemOffsets(outRect, view, parent, state)
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        for (index in 0 until parent.childCount - 1) {
            val child = parent.getChildAt(index)
            val nextChild = parent.getChildAt(index + 1)

            when (parent.getChildViewHolder(child)) {
                is PlayGameViewHolder.Footer -> c.drawRect(
                    Rect(child.left, child.bottom - dividerHeight, parent.width, child.bottom),
                    mPaint
                )
                is PlayGameViewHolder.Winner -> {
                    if (parent.getChildViewHolder(nextChild) !is PlayGameViewHolder.Winner) continue
                    c.drawRect(
                        Rect(startOffset, child.bottom - dividerHeight, parent.width, child.bottom),
                        mPaint
                    )
                }
            }
        }
    }
}
