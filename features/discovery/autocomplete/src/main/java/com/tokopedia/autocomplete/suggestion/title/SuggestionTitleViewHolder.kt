package com.tokopedia.autocomplete.suggestion.title

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocomplete.R
import com.tokopedia.utils.contentdescription.TextAndContentDescriptionUtil
import kotlinx.android.synthetic.main.layout_title_suggestion.view.*

class SuggestionTitleViewHolder(itemView: View) : AbstractViewHolder<SuggestionTitleDataView>(itemView) {

    override fun bind(element: SuggestionTitleDataView) {
        itemView.suggestionTitleTextView?.let {TextAndContentDescriptionUtil.setTextAndContentDescription(it, element.title, getString(R.string.content_desc_suggestionTitleTextView))}
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_title_suggestion
    }
}