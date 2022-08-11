package com.tokopedia.autocompletecomponent.suggestion.topshop

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.databinding.SuggestionTopShopCardLayoutBinding
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.utils.view.binding.viewBinding

class SuggestionTopShopCardViewHolder(
        itemView: View,
        private val suggestionTopShopListener: SuggestionTopShopListener
) : AbstractViewHolder<SuggestionTopShopCardDataView>(itemView) {

    companion object {
        val LAYOUT = R.layout.suggestion_top_shop_card_layout
        const val TWO_PRODUCTS = 2
        const val THREE_PRODUCTS = 3
    }
    private var binding: SuggestionTopShopCardLayoutBinding? by viewBinding()

    override fun bind(element: SuggestionTopShopCardDataView) {
        bindImage(element)
        bindIconTitle(element)
        bindTitle(element)
        bindSubtitle(element)
        bindProductImage(element)
        bindListener(element)
    }

    private fun bindImage(element: SuggestionTopShopCardDataView) {
        binding?.suggestionTopShopCardImage?.let {
            ImageHandler.loadImageCircle2(itemView.context, it, element.imageUrl)
        }
    }

    private fun bindIconTitle(item: SuggestionTopShopCardDataView) {
        binding?.suggestionTopShopCardIconTitle?.shouldShowWithAction(item.iconTitle.isNotEmpty()) {
            ImageHandler.loadImageWithoutPlaceholderAndError(binding?.suggestionTopShopCardIconTitle, item.iconTitle)
        }
    }

    private fun bindTitle(element: SuggestionTopShopCardDataView) {
        binding?.suggestionTopShopCardTitle?.shouldShowWithAction(element.title.isNotEmpty()) {
            binding?.suggestionTopShopCardTitle?.text = element.title
        }
    }

    private fun bindSubtitle(element: SuggestionTopShopCardDataView) {
        binding?.suggestionTopShopCardSubtitle?.shouldShowWithAction(element.title.isNotEmpty()) {
            binding?.suggestionTopShopCardSubtitle?.text = element.subtitle
        }
    }

    private fun bindProductImage(element: SuggestionTopShopCardDataView) {
        if (element.productData.isNotEmpty()) {
            bindProductImage1(element.productData[0].imageUrl)
            if (element.productData.size >= TWO_PRODUCTS) bindProductImage2(element.productData[1].imageUrl)
            if (element.productData.size >= THREE_PRODUCTS) bindProductImage3(element.productData[2].imageUrl)
        }
    }

    private fun bindProductImage1(imageUrl: String) {
        binding?.suggestionTopShopCardProduct1?.let {
            ImageHandler.loadImageRounded(itemView.context, it, imageUrl, 6.toDp().toFloat())
        }
    }

    private fun bindProductImage2(imageUrl: String) {
        binding?.suggestionTopShopCardProduct2?.let {
            ImageHandler.loadImageRounded(itemView.context, it, imageUrl, 6.toDp().toFloat())
        }
    }

    private fun bindProductImage3(imageUrl: String) {
        binding?.suggestionTopShopCardProduct3?.let {
            ImageHandler.loadImageRounded(itemView.context, it, imageUrl, 6.toDp().toFloat())
        }
    }

    private fun bindListener(element: SuggestionTopShopCardDataView) {
        binding?.suggestionTopShopCard?.setOnClickListener {
            suggestionTopShopListener.onTopShopCardClicked(element)
        }
    }
}