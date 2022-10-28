package com.tokopedia.autocompletecomponent.suggestion.productline

import android.graphics.Paint
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.StyleSpan
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.databinding.LayoutAutocompleteProductListItemBinding
import com.tokopedia.autocompletecomponent.suggestion.BaseSuggestionDataView
import com.tokopedia.autocompletecomponent.suggestion.SuggestionListener
import com.tokopedia.autocompletecomponent.util.safeSetSpan
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding
import java.util.*

class SuggestionProductLineViewHolder(
        itemView: View,
        private val listener: SuggestionListener
) : AbstractViewHolder<SuggestionProductLineDataDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_autocomplete_product_list_item
    }
    private var binding : LayoutAutocompleteProductListItemBinding? by viewBinding()

    private var searchQueryStartIndexInKeyword = -1

    override fun bind(item: SuggestionProductLineDataDataView) {
        setComponentHeight(item.data)
        setImage(item.data)
        setSearchQueryStartIndexInKeyword(item.data)
        setTitle(item.data)
        setLabelDiscountPercentage(item.data)
        setOriginalPrice(item.data)
        setPrice(item.data)
        setListener(item.data)
    }

    private fun setComponentHeight(item: BaseSuggestionDataView) {
        val autocompleteProductItem = binding?.autocompleteProductItem ?: return
        val layoutParams = autocompleteProductItem.layoutParams

        if (item.hasSlashedPrice()) layoutParams.height = itemView.context.resources.getDimensionPixelSize(R.dimen.autocomplete_product_triple_line_height)
        else layoutParams.height = itemView.context.resources.getDimensionPixelSize(R.dimen.autocomplete_suggestion_product_double_line_height)

        autocompleteProductItem.layoutParams = layoutParams
    }

    private fun setImage(item: BaseSuggestionDataView) {
        setImageHeight()
        bindImage(item)
    }

    private fun setImageHeight() {
        val autocompleteProductImage = binding?.autocompleteProductImage ?: return
        val layoutParams = autocompleteProductImage.layoutParams
        val resources = itemView.context.resources

        layoutParams.height = resources.getDimensionPixelSize(R.dimen.autocomplete_product_suggestion_image_size)
        layoutParams.width = resources.getDimensionPixelSize(R.dimen.autocomplete_product_suggestion_image_size)

        autocompleteProductImage.layoutParams = layoutParams
    }

    private fun bindImage(item: BaseSuggestionDataView) {
        val context = itemView.context
        binding?.autocompleteProductImage?.let {
            ImageHandler.loadImageRounded(context, it, item.imageUrl, context.resources.getDimension(R.dimen.autocomplete_product_suggestion_image_radius))
        }
    }

    private fun setSearchQueryStartIndexInKeyword(item: BaseSuggestionDataView) {
        val displayName = item.title
        val searchTerm = item.searchTerm

        searchQueryStartIndexInKeyword = if (!TextUtils.isEmpty(searchTerm)) {
            displayName.toLowerCase(Locale.getDefault()).indexOf(searchTerm.toLowerCase(Locale.getDefault()))
        } else -1
    }

    private fun setTitle(item: BaseSuggestionDataView) {
        val autocompleteProductTitle = binding?.autocompleteProductTitle ?: return
        autocompleteProductTitle.setType(Typography.BODY_2)
        autocompleteProductTitle.setWeight(Typography.REGULAR)

        if (searchQueryStartIndexInKeyword == -1) autocompleteProductTitle.text = MethodChecker.fromHtml(item.title)
        else autocompleteProductTitle.text = getHighlightedTitle(item)
    }

    private fun getHighlightedTitle(item: BaseSuggestionDataView): SpannableString {
        val highlightedTitle = SpannableString(MethodChecker.fromHtml(item.title))

        highlightTitleBeforeKeyword(highlightedTitle)

        highlightTitleAfterKeyword(highlightedTitle, item)

        return highlightedTitle
    }

    private fun highlightTitleBeforeKeyword(highlightedTitle: SpannableString) {
        highlightedTitle.safeSetSpan(
            StyleSpan(Typeface.BOLD),
            0,
            searchQueryStartIndexInKeyword,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    private fun highlightTitleAfterKeyword(
        highlightedTitle: SpannableString,
        item: BaseSuggestionDataView
    ) {
        val highlightAfterKeywordStartIndex = searchQueryStartIndexInKeyword + (item.searchTerm.length)
        val highlightAfterKeywordEndIndex = highlightedTitle.length

        highlightedTitle.safeSetSpan(
            StyleSpan(Typeface.BOLD),
            highlightAfterKeywordStartIndex,
            highlightAfterKeywordEndIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    private fun setLabelDiscountPercentage(item: BaseSuggestionDataView) {
        binding?.autocompleteProductLabelDiscountPercentage?.shouldShowWithAction(item.hasSlashedPrice()) {
            binding?.autocompleteProductLabelDiscountPercentage?.text = item.discountPercentage
        }
    }

    private fun setOriginalPrice(item: BaseSuggestionDataView) {
        val autocompleteProductOriginalPrice = binding?.autocompleteProductOriginalPrice ?: return
        autocompleteProductOriginalPrice.shouldShowWithAction(item.hasSlashedPrice()) {
            autocompleteProductOriginalPrice.setTextAndCheckShow(item.originalPrice)
            autocompleteProductOriginalPrice.paintFlags = autocompleteProductOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    private fun setPrice(item: BaseSuggestionDataView) {
        val autocompleteProductPrice = binding?.autocompleteProductPrice ?: return
        autocompleteProductPrice.setType(Typography.BODY_3)
        autocompleteProductPrice.setWeight(Typography.BOLD)

        autocompleteProductPrice.setTextAndCheckShow(item.subtitle)
    }

    private fun setListener(item: BaseSuggestionDataView) {
        binding?.autocompleteProductItem?.setOnClickListener {
            listener.onItemClicked(item)
        }
    }
}