package com.tokopedia.play.ui.productsheet.itemdecoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.play.R

/**
 * Created by jegul on 03/03/20
 */
class ProductLineItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val dividerHeight = context.resources.getDimensionPixelOffset(R.dimen.play_product_line_divider_height)
    private val startOffset = context.resources.getDimensionPixelOffset(R.dimen.play_product_line_divider_start_offset)

    private val mPaint = Paint().apply {
        color = MethodChecker.getColor(context, R.color.play_product_sheet_divider)
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)

        if (position != parent.adapter?.itemCount.orZero() - 1)
            outRect.bottom = dividerHeight
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        for (index in 0 until parent.childCount) {
            val child = parent.getChildAt(index)

            if (index != parent.childCount - 1) {
                c.drawRect(
                        Rect(startOffset, child.bottom, parent.width, child.bottom + dividerHeight),
                        mPaint
                )
            }
        }
    }


}