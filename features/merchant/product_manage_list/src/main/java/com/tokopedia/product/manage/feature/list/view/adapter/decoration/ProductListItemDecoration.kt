package com.tokopedia.product.manage.feature.list.view.adapter.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.ProductViewHolder
import kotlinx.android.synthetic.main.item_manage_product_list.view.*

class ProductListItemDecoration: RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val itemCount = state.itemCount
        val position = parent.getChildAdapterPosition(view)
        val viewHolder = parent.getChildViewHolder(view)

        if(viewHolder is ProductViewHolder && position == itemCount) {
            viewHolder.itemView.divider.hide()
        }
    }
}