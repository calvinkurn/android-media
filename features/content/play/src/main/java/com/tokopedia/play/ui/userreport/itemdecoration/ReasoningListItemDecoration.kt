package com.tokopedia.play.ui.userreport.itemdecoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.play.R

/**
 * @author by astidhiyaa on 10/12/21
 */
class ReasoningListItemDecoration(context: Context): RecyclerView.ItemDecoration() {
    private val dividerHeight = context.resources.getDimensionPixelOffset(R.dimen.play_user_report_list_divider_height)

    private val mPaint = Paint().apply {
        color = MethodChecker.getColor(context, R.color.play_dms_user_report_sheet_divider)
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)

        if (position != 0) {
            outRect.top = dividerHeight
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        for (index in 0 until parent.childCount) {
            val child = parent.getChildAt(index)

            if (index > 1) {
                c.drawRect(
                    Rect(child.left, child.top - dividerHeight, parent.width, child.top),
                    mPaint
                )
            }
        }
    }
}