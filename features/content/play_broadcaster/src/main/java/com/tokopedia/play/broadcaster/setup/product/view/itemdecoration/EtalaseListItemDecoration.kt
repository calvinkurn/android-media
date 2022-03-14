package com.tokopedia.play.broadcaster.setup.product.view.itemdecoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.setup.product.view.viewholder.EtalaseListViewHolder
import com.tokopedia.unifyprinciples.R as unifyR

/**
 * Created By : Jonathan Darwin on February 22, 2022
 */
class EtalaseListItemDecoration(
    context: Context,
) : RecyclerView.ItemDecoration() {

    private val dividerHeight = context.resources.getDimensionPixelOffset(R.dimen.play_bro_etalase_list_divider_height)

    private val mPaint = Paint().apply {
        color = MethodChecker.getColor(context, unifyR.color.Unify_N75)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        for (index in 0 until parent.childCount) {
            val child = parent.getChildAt(index)

            if (index != 0 && index != parent.childCount-1 &&
                parent.getChildViewHolder(parent.getChildAt(index+1)) !is EtalaseListViewHolder.Header) {
                val viewHolder = parent.getChildViewHolder(child)

                if (viewHolder is EtalaseListViewHolder.Body) {

                    val start = if (viewHolder.binding.tvEtalaseTitle.left <= 0) child.left
                                else viewHolder.binding.tvEtalaseTitle.left

                    c.drawRect(
                        Rect(start, child.bottom, parent.width, child.bottom + dividerHeight),
                        mPaint
                    )
                }
            }
        }
    }
}