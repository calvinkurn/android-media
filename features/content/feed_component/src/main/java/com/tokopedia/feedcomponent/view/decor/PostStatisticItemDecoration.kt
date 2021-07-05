package com.tokopedia.feedcomponent.view.decor

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker

/**
 * Created by jegul on 2019-11-26
 */
class PostStatisticItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val dividerHeight = context.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl1)
    private val dividerLeftInset = context.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl7)

    private val mPaint = Paint().apply {
        color = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N75)
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)

        if (position == 0) outRect.top = dividerHeight
        outRect.bottom = dividerHeight
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        for (index in 0 until parent.childCount) {
            val child = parent.getChildAt(index)

            if (index == 0)
                c.drawRect(
                        Rect(parent.left, child.top - dividerHeight, parent.width, child.top),
                        mPaint
                )

            val left = child.left + dividerLeftInset
            val right = parent.width

            val top = child.bottom
            val bottom = top + dividerHeight

            c.drawRect(
                    Rect(left, top ,right, bottom),
                    mPaint
            )
        }
    }
}