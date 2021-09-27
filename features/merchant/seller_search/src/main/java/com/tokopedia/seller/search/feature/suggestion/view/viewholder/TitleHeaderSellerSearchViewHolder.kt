package com.tokopedia.seller.search.feature.suggestion.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.databinding.ItemTitleHeaderSellerSearchBinding
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.TitleHeaderSellerSearchUiModel
import com.tokopedia.utils.view.binding.viewBinding

class TitleHeaderSellerSearchViewHolder(view: View) :
    AbstractViewHolder<TitleHeaderSellerSearchUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_title_header_seller_search
    }

    private val binding: ItemTitleHeaderSellerSearchBinding? by viewBinding()

    override fun bind(element: TitleHeaderSellerSearchUiModel?) {
        binding?.run {
            tvTitleHeaderSellerSearch.text = element?.title
        }
    }
}