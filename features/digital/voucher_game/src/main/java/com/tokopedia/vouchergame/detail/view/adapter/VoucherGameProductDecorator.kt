package com.tokopedia.vouchergame.detail.view.adapter

import android.content.res.Resources
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View
import com.tokopedia.vouchergame.detail.view.adapter.viewholder.VoucherGameProductCategoryViewHolder
import com.tokopedia.vouchergame.detail.view.adapter.viewholder.VoucherGameProductViewHolder

/**
 * Created by resakemal on 14/08/19.
 */
class VoucherGameProductDecorator(val space: Int, val resources: Resources) : RecyclerView.ItemDecoration() {

    private var needOffset = true

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
                        /**
                         * Add right offset if product item is on the left side of the screen
                         * while putting into account non-product items beforehand
                         */
                        if (getItemViewType(childPosition - 1) != VoucherGameProductViewHolder.LAYOUT) {
                            needOffset = true
                        }
                        if (needOffset) right = offset
                        needOffset = !needOffset
                    }
                }
            }
        }

    }

}