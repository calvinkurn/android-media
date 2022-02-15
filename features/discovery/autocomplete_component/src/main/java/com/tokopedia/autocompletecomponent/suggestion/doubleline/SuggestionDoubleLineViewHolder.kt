package com.tokopedia.autocompletecomponent.suggestion.doubleline

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
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.unifycomponents.setBodyText
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.layout_autocomplete_double_line_item.view.*
import java.util.*

class SuggestionDoubleLineViewHolder(
        itemView: View,
        private val listener: SuggestionListener
) : AbstractViewHolder<SuggestionDoubleLineDataDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_autocomplete_double_line_item

        private const val FONT_LEVEL_14_SP = 2
    }

    private var searchQueryStartIndexInKeyword = -1

    override fun bind(item: SuggestionDoubleLineDataDataView) {
        bindIconImage(item)
        bindIconTitle(item.data)
        bindIconSubtitle(item.data)
        bindSubtitle(item.data)
        bindTextTitle(item)
        bindLabel(item.data)
        bindShortcutButton(item.data)
        bindListener(item.data)
    }

    private fun bindIconImage(item: SuggestionDoubleLineDataDataView) {
        if (item.isBoldSquareType()) {
            itemView.iconImage?.loadImageRounded(item.data.imageUrl, itemView.context.resources.getDimension(R.dimen.autocomplete_product_suggestion_image_radius))
        } else {
            itemView.iconImage?.loadImageCircle(item.data.imageUrl)
        }
    }

    private fun bindIconTitle(item: BaseSuggestionDataView) {
        itemView.iconTitle?.shouldShowOrHideWithAction(item.iconTitle.isNotEmpty()) {
            ImageHandler.loadImageWithoutPlaceholderAndError(it, item.iconTitle)
        }
    }

    private fun bindIconSubtitle(item: BaseSuggestionDataView) {
        itemView.iconSubtitle?.shouldShowOrHideWithAction(item.iconSubtitle.isNotEmpty()) {
            ImageHandler.loadImageWithoutPlaceholderAndError(it, item.iconSubtitle)
        }
    }

    private fun bindSubtitle(item: BaseSuggestionDataView) {
        itemView.doubleLineSubtitle?.setTextAndCheckShow(getSubtitle(item))
    }

    private fun getSubtitle(item: BaseSuggestionDataView): String {
        val isAds = item.shopAdsDataView != null

        return if (isAds) getString(com.tokopedia.topads.sdk.R.string.title_promote_by)
        else MethodChecker.fromHtml(item.subtitle).toString()
    }

    private fun bindTextTitle(item: SuggestionDoubleLineDataDataView) {
        when {
            item.isBoldText() -> {
                setSearchQueryStartIndexInKeyword(item.data)
                bindBoldTextTitle(item.data)
            }
            item.isBoldSquareType() -> bindAllBoldTextTitle(item.data)
            else -> bindNormalTextTitle(item.data)
        }
    }

    private fun setSearchQueryStartIndexInKeyword(item: BaseSuggestionDataView) {
        val displayName = item.title
        val searchTerm = item.searchTerm

        searchQueryStartIndexInKeyword = if (!TextUtils.isEmpty(searchTerm)) {
            displayName.toLowerCase(Locale.getDefault()).indexOf(searchTerm.toLowerCase(Locale.getDefault()))
        } else -1
    }

    private fun bindBoldTextTitle(item: BaseSuggestionDataView) {
        itemView.doubleLineTitle?.weightType = Typography.BOLD
        if (searchQueryStartIndexInKeyword == -1) {
            itemView.doubleLineTitle?.text = MethodChecker.fromHtml(item.title)
        } else {
            itemView.doubleLineTitle?.text = getHighlightedTitle(item)
        }
    }

    private fun bindAllBoldTextTitle(item: BaseSuggestionDataView){
        itemView.doubleLineTitle?.setBodyText(FONT_LEVEL_14_SP, true)
        itemView.doubleLineTitle?.text = MethodChecker.fromHtml(item.title)
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

    private fun highlightTitleAfterKeyword(highlightedTitle: SpannableString, item: BaseSuggestionDataView) {
        val highlightAfterKeywordStartIndex = searchQueryStartIndexInKeyword + (item.searchTerm.length)
        val highlightAfterKeywordEndIndex = highlightedTitle.length

        highlightedTitle.safeSetSpan(
            StyleSpan(Typeface.BOLD),
            highlightAfterKeywordStartIndex,
            highlightAfterKeywordEndIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    private fun bindNormalTextTitle(item: BaseSuggestionDataView) {
        itemView.doubleLineTitle?.text = MethodChecker.fromHtml(item.title)
    }

    private fun bindLabel(item: BaseSuggestionDataView) {
        itemView.doubleLineLabel?.setTextAndCheckShow(item.label)
        if (itemView.doubleLineLabel.text.isNotEmpty()) {
            itemView.doubleLineLabel?.setLabelType(0)
        }
    }

    private fun bindShortcutButton(item: BaseSuggestionDataView) {
        itemView.actionShortcutButton?.shouldShowOrHideWithAction(item.shortcutImage.isNotEmpty()) {
            ImageHandler.loadImage2(it, item.shortcutImage, R.drawable.autocomplete_ic_copy_to_search_bar)
        }
    }

    private fun bindListener(item: BaseSuggestionDataView) {
        itemView.autocompleteDoubleLineItem?.setOnClickListener {
            listener.onItemClicked(item)
        }

        itemView.autocompleteDoubleLineItem?.addOnImpressionListener(item, object: ViewHintListener {
            override fun onViewHint() {
                listener.onItemImpressed(item)
            }
        })
    }

    private fun <T : View> T?.shouldShowOrHideWithAction(shouldShow: Boolean, action: (T) -> Unit) {
        if (this == null) return

        if (shouldShow) {
            visibility = View.VISIBLE
            action(this)
        } else {
            visibility = View.GONE
        }
    }
}