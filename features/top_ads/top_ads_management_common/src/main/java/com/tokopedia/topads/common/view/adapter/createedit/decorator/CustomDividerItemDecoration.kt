package com.tokopedia.topads.common.view.adapter.createedit.decorator

import android.graphics.Canvas
import android.graphics.Paint
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class CustomDividerItemDecoration(private val dividersToShow: List<Int>) : RecyclerView.ItemDecoration() {

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val dividerPaint = Paint()
        dividerPaint.color = ContextCompat.getColor(parent.context, unifyprinciplesR.color.Unify_NN50)

        dividerPaint.strokeWidth = 30f

        for (i in 0 until parent.childCount) {
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

