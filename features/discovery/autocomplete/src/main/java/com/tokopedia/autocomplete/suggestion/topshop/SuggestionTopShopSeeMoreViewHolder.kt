package com.tokopedia.autocomplete.suggestion.topshop

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocomplete.R
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import kotlinx.android.synthetic.main.suggestion_top_shop_see_more_layout.view.*

class SuggestionTopShopSeeMoreViewHolder(
        itemView: View,
        private val suggestionTopShopListener: SuggestionTopShopListener
) : AbstractViewHolder<SuggestionTopShopCardViewModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.suggestion_top_shop_see_more_layout
    }

    override fun bind(element: SuggestionTopShopCardViewModel) {
        bindTitle(element)
        bindListener(element)
    }

    private fun bindTitle(element: SuggestionTopShopCardViewModel) {
        itemView.suggestionTopShopSeeMoreTitle?.shouldShowWithAction(element.title.isNotEmpty()) {
            itemView.suggestionTopShopSeeMoreTitle?.text = element.title
        }
    }

    private fun bindListener(element: SuggestionTopShopCardViewModel) {
        itemView.suggestionTopShopSeeMoreCard?.setOnClickListener {
            suggestionTopShopListener.onTopShopSeeMoreClicked(element)
        }
    }
}