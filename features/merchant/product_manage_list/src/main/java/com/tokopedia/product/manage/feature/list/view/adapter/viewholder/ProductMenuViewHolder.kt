package com.tokopedia.product.manage.feature.list.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.list.view.model.ProductMenuUiModel
import kotlinx.android.synthetic.main.item_product_manage_menu.view.*

class ProductMenuViewHolder(
    itemView: View,
    private val listener: ProductMenuListener
): AbstractViewHolder<ProductMenuUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_product_manage_menu
    }

    override fun bind(menu: ProductMenuUiModel) {
        with(itemView) {
            textMenu.text = context.getString(menu.title)
            if (getString(menu.title) == getString(R.string.product_manage_create_broadcast_chat)) {
                icuPmlMoreMenu.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_bc_chat))
            } else {
                icuPmlMoreMenu.setImage(menu.icon)
            }
            setOnClickListener { listener.onClickOptionMenu(menu) }
        }
    }

    interface ProductMenuListener {
        fun onClickOptionMenu(menu: ProductMenuUiModel)
    }
}