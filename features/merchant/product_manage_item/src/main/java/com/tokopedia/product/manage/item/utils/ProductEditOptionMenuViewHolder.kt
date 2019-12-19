package com.tokopedia.product.manage.item.utils

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.tokopedia.product.manage.item.utils.ProductEditOptionMenu
import kotlinx.android.synthetic.main.item_product_edit_menu_option.view.*

class ProductEditOptionMenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun onBind(optionMenu: ProductEditOptionMenu, isSelected: Boolean) {
        itemView.titleTextView.text = optionMenu.title
        itemView.checkImageView.visibility = if (isSelected) View.VISIBLE else View.GONE
    }
}