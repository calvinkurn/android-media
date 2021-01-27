package com.tokopedia.autocomplete.suggestion.doubleline

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocomplete.R
import com.tokopedia.autocomplete.suggestion.SuggestionClickListener
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import kotlinx.android.synthetic.main.layout_autocomplete_double_line_without_image_item.view.*

class SuggestionDoubleLineWithoutImageViewHolder(
        itemView: View,
        private val clickListener: SuggestionClickListener
) : AbstractViewHolder<SuggestionDoubleLineWithoutImageViewModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_autocomplete_double_line_without_image_item
    }

    override fun bind(item: SuggestionDoubleLineWithoutImageViewModel) {
        bindTitle(item)
        bindSubtitle(item)
        bindListener(item)
    }

    private fun bindTitle(item: SuggestionDoubleLineWithoutImageViewModel) {
        itemView.autocompleteSuggestionDoubleLineWithoutImageTitle?.setTextAndCheckShow(item.title)
    }

    private fun bindSubtitle(item: SuggestionDoubleLineWithoutImageViewModel) {
        itemView.autocompleteSuggestionDoubleLineWithoutImageSubtitle?.setTextAndCheckShow(item.subtitle)
    }

    private fun bindListener(item: SuggestionDoubleLineWithoutImageViewModel) {
        itemView.autocompleteDoubleLineWithoutImageItem?.setOnClickListener {
            clickListener.onItemClicked(item)
        }
    }
}