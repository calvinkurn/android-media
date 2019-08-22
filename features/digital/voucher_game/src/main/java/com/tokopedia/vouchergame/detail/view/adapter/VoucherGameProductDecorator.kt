package com.tokopedia.vouchergame.detail.view.adapter

import android.content.res.Resources
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View
import com.tokopedia.vouchergame.detail.view.adapter.viewholder.VoucherGameProductViewHolder

/**
 * Created by resakemal on 14/08/19.
 */
class VoucherGameProductDecorator(val space: Int, val resources: Resources) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.run {
            parent.let {
                val childPosition = it.getChildAdapterPosition(view)
                val offset = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        space.toFloat(),
                        resources.displayMetrics
                ).toInt()

                // Add decorator only for product items
                it.adapter?.run {
                    if (getItemViewType(childPosition) == VoucherGameProductViewHolder.LAYOUT) {
                        bottom = offset
                        if (childPosition % 2 == 0) right = offset
                    }
                }
            }
        }

    }

}