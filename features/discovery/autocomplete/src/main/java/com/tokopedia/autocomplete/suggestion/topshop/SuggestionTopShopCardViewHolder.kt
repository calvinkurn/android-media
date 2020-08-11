package com.tokopedia.autocomplete.suggestion.topshop

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.autocomplete.R
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.unifycomponents.toDp
import kotlinx.android.synthetic.main.suggestion_top_shop_card_layout.view.*

class SuggestionTopShopCardViewHolder(
        itemView: View,
        private val suggestionTopShopListener: SuggestionTopShopListener
) : AbstractViewHolder<SuggestionTopShopCardViewModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.suggestion_top_shop_card_layout
    }

    override fun bind(element: SuggestionTopShopCardViewModel) {
        bindImage(element)
        bindIconTitle(element)
        bindTitle(element)
        bindSubtitle(element)
        bindProductImage(element)
        bindListener(element)
    }

    private fun bindImage(element: SuggestionTopShopCardViewModel) {
        itemView.suggestionTopShopCardImage?.let {
            ImageHandler.loadImageCircle2(itemView.context, it, element.imageUrl)
        }
    }

    private fun bindIconTitle(item: SuggestionTopShopCardViewModel) {
        itemView.suggestionTopShopCardIconTitle?.shouldShowWithAction(item.iconTitle.isNotEmpty()) {
            ImageHandler.loadImageWithoutPlaceholderAndError(itemView.suggestionTopShopCardIconTitle, item.iconTitle)
        }
    }

    private fun bindTitle(element: SuggestionTopShopCardViewModel) {
        itemView.suggestionTopShopCardTitle?.shouldShowWithAction(element.title.isNotEmpty()) {
            itemView.suggestionTopShopCardTitle?.text = element.title
        }
    }

    private fun bindSubtitle(element: SuggestionTopShopCardViewModel) {
        itemView.suggestionTopShopCardSubtitle?.shouldShowWithAction(element.title.isNotEmpty()) {
            itemView.suggestionTopShopCardSubtitle?.text = element.subtitle
        }
    }

    private fun bindProductImage(element: SuggestionTopShopCardViewModel) {
        if (element.products.isNotEmpty()) {
            bindProductImage1(element.products[0].imageUrl)
            if (element.products.size >= 2) bindProductImage2(element.products[1].imageUrl)
            if (element.products.size >= 3) bindProductImage3(element.products[2].imageUrl)
        }
    }

    private fun bindProductImage1(imageUrl: String) {
        itemView.suggestionTopShopCardProduct1?.let {
            ImageHandler.loadImageRounded(itemView.context, it, imageUrl, 6.toDp().toFloat())
        }
    }

    private fun bindProductImage2(imageUrl: String) {
        itemView.suggestionTopShopCardProduct2?.let {
            ImageHandler.loadImageRounded(itemView.context, it, imageUrl, 6.toDp().toFloat())
        }
    }

    private fun bindProductImage3(imageUrl: String) {
        itemView.suggestionTopShopCardProduct3?.let {
            ImageHandler.loadImageRounded(itemView.context, it, imageUrl, 6.toDp().toFloat())
        }
    }

    private fun bindListener(element: SuggestionTopShopCardViewModel) {
        itemView.suggestionTopShopCard?.setOnClickListener {
            suggestionTopShopListener.onTopShopCardClicked(element)
        }
    }
}