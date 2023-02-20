package com.tokopedia.shop.search.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.databinding.ShopSearchProductFixedResultLayoutBinding
import com.tokopedia.shop.search.view.adapter.model.ShopSearchProductFixedResultDataModel
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

class ShopSearchProductFixResultViewHolder(
    view: View
) : AbstractViewHolder<ShopSearchProductFixedResultDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.shop_search_product_fixed_result_layout
    }

    private val viewBinding: ShopSearchProductFixedResultLayoutBinding? by viewBinding()
    private val tvSearchQuery: Typography? = viewBinding?.tvSearchQuery
    private val tvSearchTypeLabel: Typography? = viewBinding?.tvSearchTypeLabel

    override fun bind(element: ShopSearchProductFixedResultDataModel) {
        tvSearchQuery?.text = element.searchQuery
        tvSearchTypeLabel?.text = element.searchTypeLabel
    }
}
