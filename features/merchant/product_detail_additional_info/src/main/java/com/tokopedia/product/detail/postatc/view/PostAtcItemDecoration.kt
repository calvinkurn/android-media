package com.tokopedia.product.detail.postatc.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.tokopedia.product.detail.common.extensions.getColorChecker
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class PostAtcItemDecoration(
    private val context: Context
) : ItemDecoration() {

    companion object {
        private const val DIVIDER_HEIGHT = 8
    }

    private val dividerColor by getDividerColor()
    private val dividerHeight by getDividerHeight()
    private val dividerPaint = Paint()

    init {
        dividerPaint.color = dividerColor
        dividerPaint.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val dividerLeft = parent.paddingLeft
        val dividerRight = parent.width - parent.paddingRight

        for (i in 0 until parent.childCount - 1) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams

            val dividerTop = child.bottom + params.bottomMargin
            val dividerBottom = dividerTop + dividerHeight

            canvas.drawRect(
                dividerLeft.toFloat(),
                dividerTop.toFloat(),
                dividerRight.toFloat(),
                dividerBottom.toFloat(),
                dividerPaint
            )
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (parent.getChildAdapterPosition(view) != 0) {
            outRect.top = dividerHeight
        }
    }

    private fun getDividerColor() = lazy {
        context.getColorChecker(id = unifyprinciplesR.color.Unify_NN50)
    }

    private fun getDividerHeight() = lazy {
        DIVIDER_HEIGHT.toPx()
    }
}
