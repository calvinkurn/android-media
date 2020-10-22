package com.tokopedia.shop.product.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.product.view.datamodel.ShopProductTitleEmptyUiModel
import kotlinx.android.synthetic.main.item_shop_product_title_empty.view.*

class ShopProductTitleEmptyViewHolder(val view: View): AbstractViewHolder<ShopProductTitleEmptyUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_shop_product_title_empty
    }
    override fun bind(element: ShopProductTitleEmptyUiModel?) {
        with(itemView) {
            tvTitle.text = getString(R.string.shop_product_search_title_empty_state)
        }
    }

}