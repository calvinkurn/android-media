package com.tokopedia.autocomplete.suggestion.doubleline

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
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.layout_autocomplete_double_line_item.view.*
import java.util.*

class SuggestionDoubleLineViewHolder(
        itemView: View,
        private val clickListener: SuggestionClickListener
) : AbstractViewHolder<SuggestionDoubleLineDataDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_autocomplete_double_line_item
    }

    private var searchQueryStartIndexInKeyword = -1

    override fun bind(item: SuggestionDoubleLineDataDataView) {
        bindIconImage(item)
        bindIconTitle(item)
        bindIconSubtitle(item)
        bindSubtitle(item)
        setSearchQueryStartIndexInKeyword(item)
        bindTextTitle(item)
        bindLabel(item)
        bindShortcutButton(item)
        bindListener(item)
    }

    private fun bindIconImage(item: SuggestionDoubleLineDataDataView) {
        itemView.iconImage?.let {
            ImageHandler.loadImageCircle2(itemView.context, it, item.imageUrl)
        }
    }

    private fun bindIconTitle(item: SuggestionDoubleLineDataDataView) {
        itemView.iconTitle?.shouldShowOrHideWithAction(item.iconTitle.isNotEmpty()) {
            ImageHandler.loadImageWithoutPlaceholderAndError(it, item.iconTitle)
        }
    }

    private fun bindIconSubtitle(item: SuggestionDoubleLineDataDataView) {
        itemView.iconSubtitle?.shouldShowOrHideWithAction(item.iconSubtitle.isNotEmpty()) {
            ImageHandler.loadImageWithoutPlaceholderAndError(it, item.iconSubtitle)
        }
    }

    private fun bindSubtitle(item: SuggestionDoubleLineDataDataView) {
        itemView.doubleLineSubtitle?.setTextAndCheckShow(MethodChecker.fromHtml(item.subtitle).toString())
    }

    private fun setSearchQueryStartIndexInKeyword(item: SuggestionDoubleLineDataDataView) {
        val displayName = item.title
        val searchTerm = item.searchTerm

        searchQueryStartIndexInKeyword = if (!TextUtils.isEmpty(searchTerm)) {
            displayName.toLowerCase(Locale.getDefault()).indexOf(searchTerm.toLowerCase(Locale.getDefault()))
        } else -1
    }

    private fun bindTextTitle(item: SuggestionDoubleLineDataDataView) {
        itemView.doubleLineTitle?.weightType = Typography.BOLD
        if (searchQueryStartIndexInKeyword == -1) {
            itemView.doubleLineTitle?.text = MethodChecker.fromHtml(item.title)
        } else {
            itemView.doubleLineTitle?.text = getHighlightedTitle(item)
        }
    }

    private fun getHighlightedTitle(item: SuggestionDoubleLineDataDataView): SpannableString {
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

    private fun highlightTitleAfterKeyword(highlightedTitle: SpannableString, item: SuggestionDoubleLineDataDataView) {
        val highlightAfterKeywordStartIndex = searchQueryStartIndexInKeyword + (item.searchTerm.length)
        val highlightAfterKeywordEndIndex = item.title.length

        highlightedTitle.safeSetSpan(
                TextAppearanceSpan(itemView.context, R.style.searchTextHiglight),
                highlightAfterKeywordStartIndex, highlightAfterKeywordEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    private fun bindLabel(item: SuggestionDoubleLineDataDataView) {
        itemView.doubleLineLabel?.setTextAndCheckShow(item.label)
        if (itemView.doubleLineLabel.text.isNotEmpty()) {
            itemView.doubleLineLabel?.setLabelType(0)
        }
    }

    private fun bindShortcutButton(item: SuggestionDoubleLineDataDataView) {
        itemView.actionShortcutButton?.shouldShowOrHideWithAction(item.shortcutImage.isNotEmpty()) {
            ImageHandler.loadImage2(it, item.shortcutImage, R.drawable.autocomplete_ic_copy_to_search_bar)
        }
    }

    private fun bindListener(item: SuggestionDoubleLineDataDataView) {
        itemView.autocompleteDoubleLineItem?.setOnClickListener {
            clickListener.onItemClicked(item)
        }
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