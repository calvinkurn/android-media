package com.tokopedia.seller.search.feature.suggestion.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.databinding.ItemDividerSellerSearchBinding
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.DividerSellerSearchUiModel
import com.tokopedia.utils.view.binding.viewBinding

class DividerSellerSearchViewHolder(view: View): AbstractViewHolder<DividerSellerSearchUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_divider_seller_search
    }

    private val binding: ItemDividerSellerSearchBinding? by viewBinding()

    override fun bind(element: DividerSellerSearchUiModel) {
        binding?.run {
            if (element.isVisible) {
                dividerSellerSearch.show()
            } else {
                dividerSellerSearch.hide()
            }
        }
    }
}