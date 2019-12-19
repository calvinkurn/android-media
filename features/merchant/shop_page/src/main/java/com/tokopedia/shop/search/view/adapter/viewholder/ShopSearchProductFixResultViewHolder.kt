package com.tokopedia.shop.search.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.search.view.adapter.model.ShopSearchProductFixedResultDataModel
import kotlinx.android.synthetic.main.item_shop_product_etalase_chip.view.*
import kotlinx.android.synthetic.main.shop_search_product_fixed_result_layout.view.*

class ShopSearchProductFixResultViewHolder(
        private var view: View
) : AbstractViewHolder<ShopSearchProductFixedResultDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.shop_search_product_fixed_result_layout
    }

    override fun bind(element: ShopSearchProductFixedResultDataModel) {
        with(view){
            tv_search_query.text = element.searchQuery
            tv_search_type_label.text = element.searchTypeLabel
        }
    }
}