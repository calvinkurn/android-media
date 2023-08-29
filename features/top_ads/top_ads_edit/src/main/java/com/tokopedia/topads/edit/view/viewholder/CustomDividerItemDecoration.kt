package com.tokopedia.topads.edit.view.viewholder

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class CustomDividerItemDecoration(private val dividersToShow: List<Int>) : RecyclerView.ItemDecoration() {

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val dividerPaint = Paint()
        dividerPaint.color = ContextCompat.getColor(parent.context, com.tokopedia.unifyprinciples.R.color.Unify_NN50)

        dividerPaint.strokeWidth = 20f

        for (i in 0 until parent.childCount - 1) {
            val child = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(child)

            if (dividersToShow.contains(position)) {
                val xStart = child.left.toFloat()
                val xEnd = child.right.toFloat()
                val y = child.bottom.toFloat()

                c.drawLine(xStart, y, xEnd, y, dividerPaint)
            }
        }
    }
}

