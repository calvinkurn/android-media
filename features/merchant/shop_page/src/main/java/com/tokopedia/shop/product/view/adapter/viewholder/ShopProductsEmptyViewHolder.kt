package com.tokopedia.shop.product.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.product.view.model.EmptyOwnShopModel
import kotlinx.android.synthetic.main.shop_products_empty_state.view.*

class ShopProductsEmptyViewHolder (val view: View): AbstractViewHolder<EmptyOwnShopModel>(view) {

    companion object {
        @JvmField
        val LAYOUT = R.layout.shop_products_empty_state
    }

    override fun bind(element: EmptyOwnShopModel) {
        with(view) {
            textViewEmptyTitle.text = element.title
            textViewEmptyContent.text = element.content
        }
    }

}