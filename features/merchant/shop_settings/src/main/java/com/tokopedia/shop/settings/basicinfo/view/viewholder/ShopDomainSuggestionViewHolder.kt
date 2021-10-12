package com.tokopedia.shop.settings.basicinfo.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.basicinfo.view.model.ShopDomainSuggestion
import com.tokopedia.shop.settings.databinding.ItemShopDomainSuggestionBinding
import com.tokopedia.utils.view.binding.viewBinding

class ShopDomainSuggestionViewHolder(
    itemView: View,
    private val onClickItemListener: (String) -> Unit
): AbstractViewHolder<ShopDomainSuggestion>(itemView) {

    private val binding: ItemShopDomainSuggestionBinding? by viewBinding()

    companion object {
        val LAYOUT = R.layout.item_shop_domain_suggestion
    }

    override fun bind(suggestion: ShopDomainSuggestion) {
        val domain = suggestion.domain
        binding?.chipSuggestion?.chipText = domain
        itemView.setOnClickListener { onClickItemListener.invoke(domain) }
    }
}