package com.tokopedia.shop.newproduct.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.newproduct.view.datamodel.ShopProductAddViewModel

/**
 * @author by alvarisi on 12/12/17.
 */

class ShopProductAddViewHolder(
        itemView: View,
        shopProductAddViewHolderListener: ShopProductAddViewHolderListener?
) : AbstractViewHolder<ShopProductAddViewModel>(itemView) {

    interface ShopProductAddViewHolderListener{
        fun onAddProductClicked()
    }

    init {
        itemView.setOnClickListener {
            shopProductAddViewHolderListener?.onAddProductClicked()
        }
    }

    override fun bind(element: ShopProductAddViewModel?) {}

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_product_add
    }
}