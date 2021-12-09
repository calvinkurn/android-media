package com.tokopedia.autocompletecomponent.suggestion.singleline

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.StyleSpan
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.suggestion.BaseSuggestionDataView
import com.tokopedia.autocompletecomponent.suggestion.SuggestionListener
import com.tokopedia.autocompletecomponent.util.safeSetSpan
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import kotlinx.android.synthetic.main.layout_autocomplete_single_line_item.view.*
import java.util.*

class SuggestionSingleLineViewHolder(
        itemView: View,
        private val listener: SuggestionListener
): AbstractViewHolder<SuggestionSingleLineDataDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_autocomplete_single_line_item
    }

    override fun bind(item: SuggestionSingleLineDataDataView) {
        bindIconImage(item.data)
        bindTextTitle(item.data)
        bindShortcutButton(item.data)
        bindListener(item.data)
    }

    private fun bindIconImage(item: BaseSuggestionDataView){
        itemView.iconImage?.let {
            ImageHandler.loadImage2(it, item.imageUrl, R.drawable.autocomplete_ic_time)
        }
    }

    private fun bindTextTitle(item: BaseSuggestionDataView){
        val startIndex = indexOfSearchQuery(item.title, item.searchTerm)
        if (startIndex == -1) {
            itemView.singleLineTitle?.text = item.title
        } else {
            val highlightedTitle = SpannableString(item.title)
            highlightedTitle.safeSetSpan(
                StyleSpan(Typeface.BOLD),
                0,
                startIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            highlightedTitle.safeSetSpan(
                StyleSpan(Typeface.BOLD),
                startIndex + item.searchTerm.length,
                item.title.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            itemView.singleLineTitle?.text = highlightedTitle
        }
    }

    private fun indexOfSearchQuery(displayName: String, searchTerm: String): Int {
        return if (!TextUtils.isEmpty(searchTerm)) {
            displayName.toLowerCase(Locale.getDefault()).indexOf(searchTerm.toLowerCase(Locale.getDefault()))
        } else -1
    }

    private fun bindShortcutButton(item: BaseSuggestionDataView){
        itemView.actionShortcutButton?.shouldShowWithAction(item.shortcutImage.isNotEmpty()) {
            ImageHandler.loadImage2(
                itemView.actionShortcutButton,
                item.shortcutImage,
                R.drawable.autocomplete_ic_copy_to_search_bar
            )
        }
    }

    private fun bindListener(item: BaseSuggestionDataView){
        itemView.autocompleteSingleLineItem?.setOnClickListener {
            listener.onItemClicked(item)
        }

        itemView.actionShortcutButton?.setOnClickListener {
            listener.copyTextToSearchView(item.title)
        }

        itemView.autocompleteSingleLineItem?.addOnImpressionListener(item, object: ViewHintListener {
            override fun onViewHint() {
                listener.onItemImpressed(item)
            }
        })
    }
}
