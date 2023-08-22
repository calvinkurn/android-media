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
import com.tokopedia.autocompletecomponent.databinding.LayoutAutocompleteDoubleLineItemBinding
import com.tokopedia.autocompletecomponent.suggestion.BaseSuggestionDataView
import com.tokopedia.autocompletecomponent.suggestion.SuggestionListener
import com.tokopedia.autocompletecomponent.util.safeSetSpan
import com.tokopedia.discovery.common.reimagine.Search1InstAuto
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding
import java.util.*

class SuggestionDoubleLineViewHolder(
    itemView: View,
    private val listener: SuggestionListener,
    private val reimagineVariant: Search1InstAuto
) : AbstractViewHolder<SuggestionDoubleLineDataDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_autocomplete_double_line_item
    }

    private val layoutStrategy: DoubleLineLayoutStrategy = DoubleLineLayoutStrategyFactory.create(reimagineVariant)

    private var binding: LayoutAutocompleteDoubleLineItemBinding? by viewBinding()

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
        bindAdText(item.data)
    }

    private fun bindIconImage(item: SuggestionDoubleLineDataDataView) {
        val iconImage = binding?.iconImage ?: return
        layoutStrategy.bindIconImage(iconImage, item)
    }

    private fun bindIconTitle(item: BaseSuggestionDataView) {
        val iconTitle = binding?.iconTitle ?: return
        val autoCompleteIconTitleReimagine = binding?.autoCompleteIconTitleReimagine ?: return

        layoutStrategy.bindIconTitle(iconTitle, autoCompleteIconTitleReimagine, item)
    }

    private fun bindIconSubtitle(item: BaseSuggestionDataView) {
        binding?.iconSubtitle?.shouldShowOrHideWithAction(item.iconSubtitle.isNotEmpty()) {
            ImageHandler.loadImageWithoutPlaceholderAndError(it, item.iconSubtitle)
        }
    }

    private fun bindSubtitle(item: BaseSuggestionDataView) {
        binding?.doubleLineSubtitle?.setTextAndCheckShow(getSubtitle(item))
    }

    private fun getSubtitle(item: BaseSuggestionDataView): String {
        return MethodChecker.fromHtml(item.subtitle).toString()
    }

    private fun bindTextTitle(item: SuggestionDoubleLineDataDataView) {
        val doubleLineTitle = binding?.doubleLineTitle ?: return
        layoutStrategy.bindTitle(doubleLineTitle)
        when {
            item.data.isBoldAllText() -> {
                bindAllBoldTextTitle(item.data)
            }

            else -> {
                setSearchQueryStartIndexInKeyword(item.data)
                bindBoldTextTitle(item.data)
            }
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
        val doubleLineTitle = binding?.doubleLineTitle ?: return
        if (searchQueryStartIndexInKeyword == -1) {
            doubleLineTitle.setWeight(Typography.BOLD)
            doubleLineTitle.text = MethodChecker.fromHtml(item.title)
        } else {
            doubleLineTitle.setWeight(Typography.REGULAR)
            doubleLineTitle.text = getHighlightedTitle(item)
        }
    }

    private fun bindAllBoldTextTitle(item: BaseSuggestionDataView) {
        val doubleLineTitle = binding?.doubleLineTitle ?: return
        doubleLineTitle.setWeight(Typography.BOLD)
        doubleLineTitle.text = MethodChecker.fromHtml(item.title)
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

    private fun bindLabel(item: BaseSuggestionDataView) {
        val doubleLineLabel = binding?.doubleLineLabel ?: return
        doubleLineLabel.setTextAndCheckShow(item.label)
        if (doubleLineLabel.text.isNotEmpty()) {
            doubleLineLabel.setLabelType(0)
        }
    }

    private fun bindShortcutButton(item: BaseSuggestionDataView) {
        binding?.actionShortcutButton?.shouldShowOrHideWithAction(item.shortcutImage.isNotEmpty()) {
            ImageHandler.loadImage2(it, item.shortcutImage, R.drawable.autocomplete_ic_copy_to_search_bar)
        }
    }

    private fun bindListener(item: BaseSuggestionDataView) {
        binding?.autocompleteDoubleLineItem?.setOnClickListener {
            listener.onItemClicked(item)
        }

        binding?.autocompleteDoubleLineItem?.addOnImpressionListener(
            item,
            object : ViewHintListener {
                override fun onViewHint() {
                    listener.onItemImpressed(item)
                }
            }
        )
    }

    private fun bindAdText(item: BaseSuggestionDataView) {
        val adText = binding?.adText ?: return
        val dotImage = binding?.autoCompleteDotAds ?: return
        layoutStrategy.bindAdsLabel(adText, dotImage, item)
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
