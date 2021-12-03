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
import com.tokopedia.autocompletecomponent.suggestion.BaseSuggestionDataView
import com.tokopedia.autocompletecomponent.suggestion.SuggestionListener
import com.tokopedia.autocompletecomponent.util.safeSetSpan
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.layout_autocomplete_product_list_item.view.*
import java.util.*

class SuggestionProductLineViewHolder(
        itemView: View,
        private val listener: SuggestionListener
) : AbstractViewHolder<SuggestionProductLineDataDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_autocomplete_product_list_item
    }

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
        val layoutParams = itemView.autocompleteProductItem.layoutParams

        if (item.hasSlashedPrice()) layoutParams.height = itemView.context.resources.getDimensionPixelSize(R.dimen.autocomplete_product_triple_line_height)
        else layoutParams.height = itemView.context.resources.getDimensionPixelSize(R.dimen.autocomplete_suggestion_product_double_line_height)

        itemView.autocompleteProductItem.layoutParams = layoutParams
    }

    private fun setImage(item: BaseSuggestionDataView) {
        setImageHeight()
        bindImage(item)
    }

    private fun setImageHeight() {
        val layoutParams = itemView.autocompleteProductImage.layoutParams
        val resources = itemView.context.resources

        layoutParams.height = resources.getDimensionPixelSize(R.dimen.autocomplete_product_suggestion_image_size)
        layoutParams.width = resources.getDimensionPixelSize(R.dimen.autocomplete_product_suggestion_image_size)

        itemView.autocompleteProductImage.layoutParams = layoutParams
    }

    private fun bindImage(item: BaseSuggestionDataView) {
        val context = itemView.context
        itemView.autocompleteProductImage?.let {
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
        itemView.autocompleteProductTitle?.setType(Typography.BODY_2)
        itemView.autocompleteProductTitle?.setWeight(Typography.REGULAR)

        if (searchQueryStartIndexInKeyword == -1) itemView.autocompleteProductTitle?.text = MethodChecker.fromHtml(item.title)
        else itemView.autocompleteProductTitle?.text = getHighlightedTitle(item)
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
        val highlightAfterKeywordEndIndex = item.title.length

        highlightedTitle.safeSetSpan(
            StyleSpan(Typeface.BOLD),
            highlightAfterKeywordStartIndex,
            highlightAfterKeywordEndIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    private fun setLabelDiscountPercentage(item: BaseSuggestionDataView) {
        itemView.autocompleteProductLabelDiscountPercentage?.shouldShowWithAction(item.hasSlashedPrice()) {
            itemView.autocompleteProductLabelDiscountPercentage?.text = item.discountPercentage
        }
    }

    private fun setOriginalPrice(item: BaseSuggestionDataView) {
        itemView.autocompleteProductOriginalPrice?.shouldShowWithAction(item.hasSlashedPrice()) {
            itemView.autocompleteProductOriginalPrice?.setTextAndCheckShow(item.originalPrice)
            itemView.autocompleteProductOriginalPrice?.paintFlags = itemView.autocompleteProductOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    private fun setPrice(item: BaseSuggestionDataView) {
        itemView.autocompleteProductPrice?.setType(Typography.BODY_3)
        itemView.autocompleteProductPrice?.setWeight(Typography.BOLD)

        itemView.autocompleteProductPrice?.setTextAndCheckShow(item.subtitle)
    }

    private fun setListener(item: BaseSuggestionDataView) {
        itemView.autocompleteProductItem?.setOnClickListener {
            listener.onItemClicked(item)
        }
    }
}