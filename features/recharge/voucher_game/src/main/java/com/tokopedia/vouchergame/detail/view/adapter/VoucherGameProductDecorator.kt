package com.tokopedia.vouchergame.detail.view.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.vouchergame.detail.view.adapter.viewholder.VoucherGameProductViewHolder

/**
 * Created by resakemal on 14/08/19.
 */
class VoucherGameProductDecorator(private val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.apply {
            val childPosition = parent.getChildAdapterPosition(view)

            // Add decorator only for product items
            parent.adapter?.let {
                if (it.getItemViewType(childPosition) == VoucherGameProductViewHolder.LAYOUT) {
                    bottom = space
                    right = space
                }
            }
        }
    }
}