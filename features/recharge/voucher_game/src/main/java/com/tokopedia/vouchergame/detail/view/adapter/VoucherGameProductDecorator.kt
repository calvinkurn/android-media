package com.tokopedia.vouchergame.detail.view.adapter

import android.content.res.Resources
import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.util.TypedValue
import android.view.View
import com.tokopedia.vouchergame.detail.view.adapter.viewholder.VoucherGameProductCategoryViewHolder
import com.tokopedia.vouchergame.detail.view.adapter.viewholder.VoucherGameProductViewHolder

/**
 * Created by resakemal on 14/08/19.
 */
class VoucherGameProductDecorator(val space: Int) : RecyclerView.ItemDecoration() {

    private var needOffset = true

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.run {
            parent.let {
                val childPosition = it.getChildAdapterPosition(view)

                // Add decorator only for product items
                it.adapter?.run {
                    if (getItemViewType(childPosition) == VoucherGameProductViewHolder.LAYOUT) {
                        bottom = space
                        /**
                         * Add right space if product item is on the left side of the screen
                         * while putting into account non-product items beforehand
                         */
                        if (getItemViewType(childPosition - 1) != VoucherGameProductViewHolder.LAYOUT) {
                            needOffset = true
                        }
                        if (needOffset) right = space
                        needOffset = !needOffset
                    }
                }
            }
        }

    }

}