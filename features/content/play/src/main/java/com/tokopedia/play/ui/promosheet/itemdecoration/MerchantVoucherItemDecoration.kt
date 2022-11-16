package com.tokopedia.play.ui.promosheet.itemdecoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.play.R
import com.tokopedia.unifyprinciples.R as unifyR
import com.tokopedia.play.ui.promosheet.viewholder.MerchantVoucherNewViewHolder

/**
 * Created by jegul on 03/03/20
 */
class MerchantVoucherItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val dividerHeight = context.resources.getDimensionPixelOffset(R.dimen.play_product_line_divider_height)
    private val space16 = context.resources.getDimensionPixelOffset(unifyR.dimen.spacing_lvl4)

    private val mPaint = Paint().apply {
        color = MethodChecker.getColor(context, R.color.play_dms_product_sheet_divider)
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)

        when {
            position > 0 -> {
                outRect.top = dividerHeight
            }
            position == 0 -> {
                outRect.top = space16
            }
            else -> super.getItemOffsets(outRect, view, parent, state)
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        for (index in 0 until parent.childCount - 1) {
            val child = parent.getChildAt(index)

            when (parent.getChildViewHolder(child)) {
                is MerchantVoucherNewViewHolder -> {
                    c.drawRect(
                        Rect(child.left, child.bottom - dividerHeight, child.right, child.bottom),
                        mPaint
                    )
                }
            }
        }
    }
}