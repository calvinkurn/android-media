package com.tokopedia.product.manage.feature.list.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.manage.R
import kotlinx.android.synthetic.main.item_product_manage_multi_edit.view.*

class MultiEditViewHolder(itemView: View, private val listener: MenuClickListener): RecyclerView.ViewHolder(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_product_manage_multi_edit
    }

    fun bind(titleId: Int) {
        itemView.textMenu.text = itemView.context.getString(titleId)
        itemView.setOnClickListener { listener.onClickMenuItem(titleId) }
    }

    interface MenuClickListener {
        fun onClickMenuItem(menuId: Int)
    }
}