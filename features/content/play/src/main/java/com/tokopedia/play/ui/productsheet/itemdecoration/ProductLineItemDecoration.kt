package com.tokopedia.play.ui.productsheet.itemdecoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.play.R
import com.tokopedia.play.ui.productsheet.viewholder.ProductLineViewHolder

/**
 * Created by jegul on 03/03/20
 */
class ProductLineItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val dividerHeight = context.resources.getDimensionPixelOffset(R.dimen.play_product_line_divider_height)
    private val startOffset = context.resources.getDimensionPixelOffset(R.dimen.play_product_line_divider_start_offset)

    private val mPaint = Paint().apply {
        color = MethodChecker.getColor(context, R.color.play_dms_product_sheet_divider)
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)

        if (position != 0) {
            outRect.top = dividerHeight
        } else super.getItemOffsets(outRect, view, parent, state)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        for (index in 0 until parent.childCount) {
            val child = parent.getChildAt(index)

            if (index != 0) {
                val viewHolder = parent.getChildViewHolder(child)

                if (viewHolder is ProductLineViewHolder) {
                    val start = if (viewHolder.tvProductTitle.left <= 0) startOffset else viewHolder.tvProductTitle.left

                    c.drawRect(
                            Rect(start, child.top - dividerHeight, parent.width, child.top),
                            mPaint
                    )
                } else {
                    c.drawRect(
                            Rect(startOffset, child.top - dividerHeight, parent.width, child.top),
                            mPaint
                    )
                }
            }
        }
    }


}