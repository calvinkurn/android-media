package com.tokopedia.autocomplete.suggestion

import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.TextAppearanceSpan
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.autocomplete.R
import kotlinx.android.synthetic.main.layout_autocomplete_single_line_item.view.*
import java.util.*

class SuggestionSingleLineViewHolder(
        itemView: View,
        private val clickListener: SuggestionClickListener
): AbstractViewHolder<SuggestionSingleLineViewModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_autocomplete_single_line_item
    }

    override fun bind(item: SuggestionSingleLineViewModel) {
        bindIconImage(item)
        bindTextTitle(item)
        bindShortcutButton(item)
        bindListener(item)
    }

    private fun bindIconImage(item: SuggestionSingleLineViewModel){
        itemView.iconImage?.let {
            ImageHandler.loadImage2(it, item.imageUrl, R.drawable.autocomplete_ic_time)
        }
    }

    private fun bindTextTitle(item: SuggestionSingleLineViewModel){
        val startIndex = indexOfSearchQuery(item.title, item.searchTerm)
        if (startIndex == -1) {
            itemView.singleLineTitle?.text = item.title.toLowerCase()
        } else {
            val highlightedTitle = SpannableString(item.title)
            highlightedTitle.setSpan(TextAppearanceSpan(itemView.context, R.style.searchTextHiglight),
                    0, startIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            highlightedTitle.setSpan(TextAppearanceSpan(itemView.context, R.style.searchTextHiglight),
                    startIndex + item.searchTerm.length,
                    item.title.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            itemView.singleLineTitle?.text = highlightedTitle
        }
    }

    private fun indexOfSearchQuery(displayName: String, searchTerm: String): Int {
        return if (!TextUtils.isEmpty(searchTerm)) {
            displayName.toLowerCase(Locale.getDefault()).indexOf(searchTerm.toLowerCase(Locale.getDefault()))
        } else -1
    }

    private fun bindShortcutButton(item: SuggestionSingleLineViewModel){
        itemView.actionShortcutButton?.let {
            ImageHandler.loadImage2(it, item.shortcutImage, R.drawable.autocomplete_ic_copy_to_search_bar)
        }
    }

    private fun bindListener(item: SuggestionSingleLineViewModel){
        itemView.autocompleteSingleLineItem?.setOnClickListener {
//            AutocompleteTracking.eventClickAutoCompleteSearch(
//                    itemView.context,
//                    String.format(
//                            "keyword: %s - value: %s - po: %s - applink: %s",
//                            item.title,
//                            item.searchTerm,
//                            (adapterPosition + 1).toString(),
//                            item.applink
//                    ),
//                    ""
//            )
            clickListener.onItemClicked(item.applink, item.url)
        }

        itemView.actionShortcutButton?.setOnClickListener {
            clickListener.copyTextToSearchView(item.title)
        }
    }
}