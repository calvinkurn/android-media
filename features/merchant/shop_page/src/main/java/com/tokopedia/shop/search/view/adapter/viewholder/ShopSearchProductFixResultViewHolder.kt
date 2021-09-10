package com.tokopedia.shop.search.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.search.view.adapter.model.ShopSearchProductFixedResultDataModel
import com.tokopedia.unifyprinciples.Typography

class ShopSearchProductFixResultViewHolder(
        view: View
) : AbstractViewHolder<ShopSearchProductFixedResultDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.shop_search_product_fixed_result_layout
    }

    private val tvSearchQuery: Typography? = view.findViewById(R.id.tv_search_query)
    private val tvSearchTypeLabel: Typography? = view.findViewById(R.id.tv_search_type_label)

    override fun bind(element: ShopSearchProductFixedResultDataModel) {
        tvSearchQuery?.text = element.searchQuery
        tvSearchTypeLabel?.text = element.searchTypeLabel
    }
}