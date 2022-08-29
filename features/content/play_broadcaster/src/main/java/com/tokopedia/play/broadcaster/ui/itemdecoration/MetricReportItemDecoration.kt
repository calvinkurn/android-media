package com.tokopedia.play.broadcaster.ui.itemdecoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.viewholder.TrafficMetricViewHolder
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.R as unifyR

/**
 * Created By : Jonathan Darwin on March 07, 2022
 */
class MetricReportItemDecoration(
    context: Context,
) : RecyclerView.ItemDecoration() {

    private val dividerHeight = context.resources.getDimensionPixelOffset(R.dimen.play_bro_summary_metric_divider_height)

    private val mPaint = Paint().apply {
        color = MethodChecker.getColor(context, unifyR.color.Unify_NN200)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        for (index in 0 until parent.childCount) {
            val child = parent.getChildAt(index)
            val viewHolder = parent.getChildViewHolder(child)

            if (viewHolder is TrafficMetricViewHolder) {
                val tvMetric = viewHolder.binding.tvItemPlaySummaryDescription

                val start = if (tvMetric.left <= 0) child.left
                else tvMetric.left

                c.drawRect(
                    Rect(start, child.bottom, parent.width, child.bottom + dividerHeight),
                    mPaint
                )
            }
        }
    }
}