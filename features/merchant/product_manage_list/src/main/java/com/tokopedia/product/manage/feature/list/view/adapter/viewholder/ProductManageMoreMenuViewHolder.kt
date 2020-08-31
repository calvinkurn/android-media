package com.tokopedia.product.manage.feature.list.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.list.view.model.ProductMoreMenuModel
import kotlinx.android.synthetic.main.item_product_manage_more_menu.view.*

class ProductManageMoreMenuViewHolder(itemView: View, private val listener: ProductManageMoreMenuListener) : RecyclerView.ViewHolder(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_product_manage_more_menu
    }

    fun bind(menu: ProductMoreMenuModel) {
        itemView.tv_more_menu_title.text = menu.title
        itemView.setOnClickListener {
            listener.onMoreMenuClicked(menu)
        }
    }

    interface ProductManageMoreMenuListener {
        fun onMoreMenuClicked(menu: ProductMoreMenuModel)
    }
}