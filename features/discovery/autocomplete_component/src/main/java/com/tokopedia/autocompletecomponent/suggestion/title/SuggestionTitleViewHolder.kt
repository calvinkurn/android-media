package com.tokopedia.autocompletecomponent.suggestion.title

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.databinding.LayoutTitleSuggestionBinding
import com.tokopedia.utils.contentdescription.TextAndContentDescriptionUtil
import com.tokopedia.utils.view.binding.viewBinding

class SuggestionTitleViewHolder(itemView: View) : AbstractViewHolder<SuggestionTitleDataView>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_title_suggestion
    }
    private var binding: LayoutTitleSuggestionBinding? by viewBinding()

    override fun bind(element: SuggestionTitleDataView) {
        binding?.suggestionTitleTextView?.let {
            TextAndContentDescriptionUtil.setTextAndContentDescription(it, element.title, getString(R.string.content_desc_suggestionTitleTextView))
        }
    }
}