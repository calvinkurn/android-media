package com.tokopedia.shop.product.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.product.view.datamodel.ShopProductEmptySearchUiModel
import com.tokopedia.shop.product.view.listener.ShopProductEmptySearchListener
import kotlinx.android.synthetic.main.shop_search_product_empty_state.view.*

class ShopProductEmptySearchViewHolder(val view: View,
                                       private val shopProductEmptySearchListener: ShopProductEmptySearchListener?): AbstractViewHolder<ShopProductEmptySearchUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.shop_search_product_empty_state
    }
    override fun bind(element: ShopProductEmptySearchUiModel) {
        with(itemView) {
            shopSearchEmptyState?.setPrimaryCTAClickListener {
                shopProductEmptySearchListener?.onPrimaryButtonEmptyClicked()
            }
            shopSearchEmptyState?.setSecondaryCTAClickListener {
                shopProductEmptySearchListener?.onSecondaryButtonEmptyClicked()
            }
        }
    }

}