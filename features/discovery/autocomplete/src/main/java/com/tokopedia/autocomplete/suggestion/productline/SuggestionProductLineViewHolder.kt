package com.tokopedia.autocomplete.suggestion.productline

import android.graphics.Paint
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.TextAppearanceSpan
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.autocomplete.R
import com.tokopedia.autocomplete.suggestion.SuggestionClickListener
import com.tokopedia.autocomplete.util.safeSetSpan
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.layout_autocomplete_product_list_item.view.*
import java.util.*

class SuggestionProductLineViewHolder(
        itemView: View,
        private val clickListener: SuggestionClickListener
) : AbstractViewHolder<SuggestionProductLineDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_autocomplete_product_list_item
    }

    private var searchQueryStartIndexInKeyword = -1

    override fun bind(item: SuggestionProductLineDataView) {
        setComponentHeight(item)
        setImage(item)
        setSearchQueryStartIndexInKeyword(item)
        setTitle(item)
        setTitleMargin(item)
        setLabelDiscountPercentage(item)
        setOriginalPrice(item)
        setPrice(item)
        setPriceMargin(item)
        setListener(item)
    }

    private fun setComponentHeight(item: SuggestionProductLineDataView) {
        val layoutParams = itemView.autocompleteProductItem.layoutParams

        if (item.isTripleLine()) layoutParams.height = itemView.context.resources.getDimensionPixelSize(R.dimen.autocomplete_product_triple_line_height)
        else layoutParams.height = itemView.context.resources.getDimensionPixelSize(R.dimen.autocomplete_product_double_line_height)

        itemView.autocompleteProductItem.layoutParams = layoutParams
    }

    private fun SuggestionProductLineDataView.isTripleLine(): Boolean {
        return discountPercentage.isNotEmpty() && originalPrice.isNotEmpty();
    }

    private fun setImage(item: SuggestionProductLineDataView) {
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

    private fun bindImage(item: SuggestionProductLineDataView) {
        val context = itemView.context
        itemView.autocompleteProductImage?.let {
            ImageHandler.loadImageRounded(context, it, item.imageUrl, context.resources.getDimension(R.dimen.autocomplete_product_suggestion_image_radius))
        }
    }

    private fun setSearchQueryStartIndexInKeyword(item: SuggestionProductLineDataView) {
        val displayName = item.title
        val searchTerm = item.searchTerm

        searchQueryStartIndexInKeyword = if (!TextUtils.isEmpty(searchTerm)) {
            displayName.toLowerCase(Locale.getDefault()).indexOf(searchTerm.toLowerCase(Locale.getDefault()))
        } else -1
    }

    private fun setTitle(item: SuggestionProductLineDataView) {
        itemView.autocompleteProductTitle?.fontType = Typography.BODY_2
        itemView.autocompleteProductTitle?.weightType = Typography.REGULAR

        if (searchQueryStartIndexInKeyword == -1) itemView.autocompleteProductTitle?.text = MethodChecker.fromHtml(item.title)
        else itemView.autocompleteProductTitle?.text = getHighlightedTitle(item)
    }

    private fun getHighlightedTitle(item: SuggestionProductLineDataView): SpannableString {
        val highlightedTitle = SpannableString(MethodChecker.fromHtml(item.title))

        highlightTitleBeforeKeyword(highlightedTitle)

        highlightTitleAfterKeyword(highlightedTitle, item)

        return highlightedTitle
    }

    private fun highlightTitleBeforeKeyword(highlightedTitle: SpannableString) {
        highlightedTitle.safeSetSpan(
                TextAppearanceSpan(itemView.context, R.style.searchTextHiglight),
                0, searchQueryStartIndexInKeyword, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    private fun highlightTitleAfterKeyword(highlightedTitle: SpannableString, item: SuggestionProductLineDataView) {
        val highlightAfterKeywordStartIndex = searchQueryStartIndexInKeyword + (item.searchTerm.length)
        val highlightAfterKeywordEndIndex = item.title.length

        highlightedTitle.safeSetSpan(
                TextAppearanceSpan(itemView.context, R.style.searchTextHiglight),
                highlightAfterKeywordStartIndex, highlightAfterKeywordEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    private fun setTitleMargin(item: SuggestionProductLineDataView) {
        if (!item.isTripleLine()) {
            val resources = itemView.context.resources
            itemView.autocompleteProductTitle?.setMargin(
                    resources.getDimensionPixelSize(R.dimen.autocomplete_product_double_line_title_margin_left),
                    resources.getDimensionPixelSize(R.dimen.autocomplete_product_double_line_title_margin_top),
                    resources.getDimensionPixelSize(R.dimen.autocomplete_product_double_line_title_margin_right),
                    resources.getDimensionPixelSize(R.dimen.autocomplete_product_double_line_title_margin_bottom)
            )
        }
    }

    private fun setLabelDiscountPercentage(item: SuggestionProductLineDataView) {
        itemView.autocompleteProductLabelDiscountPercentage?.shouldShowWithAction(item.discountPercentage.isNotEmpty()) {
            itemView.autocompleteProductLabelDiscountPercentage?.text = item.discountPercentage
        }
    }

    private fun setOriginalPrice(item: SuggestionProductLineDataView) {
        itemView.autocompleteProductOriginalPrice?.shouldShowWithAction(item.originalPrice.isNotEmpty()) {
            itemView.autocompleteProductOriginalPrice?.setTextAndCheckShow(item.originalPrice)
            itemView.autocompleteProductOriginalPrice?.paintFlags = itemView.autocompleteProductOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    private fun setPrice(item: SuggestionProductLineDataView) {
        itemView.autocompleteProductPrice?.fontType = Typography.BODY_3
        itemView.autocompleteProductPrice?.weightType = Typography.BOLD

        itemView.autocompleteProductPrice?.setTextAndCheckShow(item.subtitle)
    }

    private fun setPriceMargin(item: SuggestionProductLineDataView) {
        if (!item.isTripleLine()) {
            val resources = itemView.context.resources
            itemView.autocompleteProductPrice?.setMargin(
                    resources.getDimensionPixelSize(R.dimen.autocomplete_product_double_line_price_margin_left),
                    resources.getDimensionPixelSize(R.dimen.autocomplete_product_double_line_price_margin_top),
                    resources.getDimensionPixelSize(R.dimen.autocomplete_product_double_line_price_margin_right),
                    resources.getDimensionPixelSize(R.dimen.autocomplete_product_double_line_price_margin_bottom)
            )
        }
    }

    private fun setListener(item: SuggestionProductLineDataView) {
        itemView.autocompleteProductItem?.setOnClickListener {
            clickListener.onItemClicked(item)
        }
    }
}