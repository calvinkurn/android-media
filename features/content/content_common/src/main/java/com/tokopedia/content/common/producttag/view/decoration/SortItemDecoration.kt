package com.tokopedia.content.common.producttag.view.decoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.content.common.R
import com.tokopedia.content.common.producttag.view.adapter.SortAdapter
import com.tokopedia.unifyprinciples.R as unifyR

/**
 * Created By : Jonathan Darwin on May 20, 2022
 */
class SortItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val dividerHeight = context.resources.getDimensionPixelOffset(R.dimen.content_common_space_1)

    private val mPaint = Paint().apply {
        color = MethodChecker.getColor(context, unifyR.color.Unify_N75)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        for (index in 0 until parent.childCount) {
            val child = parent.getChildAt(index)

            if (index != parent.childCount-1) {
                val viewHolder = parent.getChildViewHolder(child)

                if(viewHolder is SortAdapter.ViewHolder) {
                    val start = if (viewHolder.binding.tvSortName.left <= 0) child.left
                    else viewHolder.binding.tvSortName.left

                    c.drawRect(
                        Rect(start, child.bottom, parent.width, child.bottom + dividerHeight),
                        mPaint
                    )
                }
            }
        }
    }
}