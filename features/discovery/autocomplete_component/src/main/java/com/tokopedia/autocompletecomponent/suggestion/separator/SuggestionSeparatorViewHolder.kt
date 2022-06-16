package com.tokopedia.autocompletecomponent.suggestion.separator

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.databinding.LayoutAutocompleteSuggestionSeparatorBinding
import com.tokopedia.utils.view.binding.viewBinding

class SuggestionSeparatorViewHolder(itemView: View) : AbstractViewHolder<SuggestionSeparatorDataView>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_autocomplete_suggestion_separator
    }
    private var binding : LayoutAutocompleteSuggestionSeparatorBinding? by viewBinding()

    override fun bind(element: SuggestionSeparatorDataView) {
        binding?.autocompleteSuggestionDoubleLineWithoutImageSeparator?.visibility = View.VISIBLE
    }
}