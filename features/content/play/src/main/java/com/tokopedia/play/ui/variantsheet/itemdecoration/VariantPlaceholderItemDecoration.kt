package com.tokopedia.play.ui.variantsheet.itemdecoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.play.R
import com.tokopedia.play.ui.variantsheet.adapter.viewholder.VariantPlaceholderViewHolder

/**
 * Created by jegul on 15/03/20
 */
class VariantPlaceholderItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val dividerHeight = context.resources.getDimensionPixelOffset(R.dimen.play_placeholder_variant_divider_height)
    private val startOffset = context.resources.getDimensionPixelOffset(R.dimen.spacing_lvl4)

    private val mPaint = Paint().apply {
        color = MethodChecker.getColor(context, R.color.play_variant_placeholder_divider)
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val viewHolder = parent.getChildViewHolder(view)

        if (position != 0 && viewHolder is VariantPlaceholderViewHolder) {
            outRect.top = dividerHeight
        } else super.getItemOffsets(outRect, view, parent, state)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        for (index in 0 until parent.childCount) {
            val child = parent.getChildAt(index)

            if (index != 0) {
                val viewHolder = parent.getChildViewHolder(child)
                if (viewHolder is VariantPlaceholderViewHolder) {
                    c.drawRect(
                            Rect(startOffset, child.bottom, parent.width, child.bottom + dividerHeight),
                            mPaint
                    )
                }
            }
        }
    }
}