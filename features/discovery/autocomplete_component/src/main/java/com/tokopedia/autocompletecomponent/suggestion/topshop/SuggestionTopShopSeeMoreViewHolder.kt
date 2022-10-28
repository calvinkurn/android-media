package com.tokopedia.autocompletecomponent.suggestion.topshop

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.databinding.SuggestionTopShopSeeMoreLayoutBinding
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.utils.view.binding.viewBinding

class SuggestionTopShopSeeMoreViewHolder(
        itemView: View,
        private val suggestionTopShopListener: SuggestionTopShopListener
) : AbstractViewHolder<SuggestionTopShopCardDataView>(itemView) {

    companion object {
        val LAYOUT = R.layout.suggestion_top_shop_see_more_layout
    }
    private var binding: SuggestionTopShopSeeMoreLayoutBinding? by viewBinding()

    override fun bind(element: SuggestionTopShopCardDataView) {
        bindTitle(element)
        bindListener(element)
    }

    private fun bindTitle(element: SuggestionTopShopCardDataView) {
        binding?.suggestionTopShopSeeMoreTitle?.shouldShowWithAction(element.title.isNotEmpty()) {
            binding?.suggestionTopShopSeeMoreTitle?.text = element.title
        }
    }

    private fun bindListener(element: SuggestionTopShopCardDataView) {
        binding?.suggestionTopShopSeeMoreCard?.setOnClickListener {
            suggestionTopShopListener.onTopShopSeeMoreClicked(element)
        }
    }
}