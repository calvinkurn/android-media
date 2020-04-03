package com.tokopedia.product.manage.feature.list.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.list.view.model.ProductMenuViewModel
import kotlinx.android.synthetic.main.item_product_manage_menu.view.*

class ProductMenuViewHolder(
    itemView: View,
    private val listener: ProductMenuListener
): AbstractViewHolder<ProductMenuViewModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_product_manage_menu
    }

    override fun bind(menu: ProductMenuViewModel) {
        itemView.textMenu.text = itemView.context.getString(menu.title)
        itemView.textMenu.setCompoundDrawablesWithIntrinsicBounds(menu.icon, 0, 0, 0)
        itemView.setOnClickListener { listener.onClickOptionMenu(menu) }
    }

    interface ProductMenuListener {
        fun onClickOptionMenu(menu: ProductMenuViewModel)
    }
}