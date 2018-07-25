package com.tokopedia.product.edit.util

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.item_product_edit_menu_option.view.*

class ProductEditOptionMenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun onBind(optionMenu: ProductEditOptionMenu, isSelected: Boolean) {
        itemView.titleTextView.text = optionMenu.title
        if (isSelected) {
            itemView.checkImageView.visibility = View.VISIBLE
        } else {
            itemView.checkImageView.visibility = View.GONE
        }
    }
}