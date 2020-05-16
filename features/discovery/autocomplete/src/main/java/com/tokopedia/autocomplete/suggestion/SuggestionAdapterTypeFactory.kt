package com.tokopedia.autocomplete.suggestion

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocomplete.suggestion.doubleline.SuggestionDoubleLineViewHolder
import com.tokopedia.autocomplete.suggestion.doubleline.SuggestionDoubleLineViewModel
import com.tokopedia.autocomplete.suggestion.singleline.SuggestionSingleLineViewHolder
import com.tokopedia.autocomplete.suggestion.singleline.SuggestionSingleLineViewModel
import com.tokopedia.autocomplete.suggestion.title.SuggestionTitleViewHolder
import com.tokopedia.autocomplete.suggestion.title.SuggestionTitleViewModel

class SuggestionAdapterTypeFactory(private val clickListener: SuggestionClickListener) : BaseAdapterTypeFactory(), SuggestionTypeFactory {

    override fun type(viewModel: SuggestionTitleViewModel): Int {
        return SuggestionTitleViewHolder.LAYOUT
    }

    override fun type(viewModel: SuggestionSingleLineViewModel): Int {
        return SuggestionSingleLineViewHolder.LAYOUT
    }

    override fun type(viewModel: SuggestionDoubleLineViewModel): Int {
        return SuggestionDoubleLineViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            SuggestionTitleViewHolder.LAYOUT -> SuggestionTitleViewHolder(parent)
            SuggestionSingleLineViewHolder.LAYOUT -> SuggestionSingleLineViewHolder(parent, clickListener)
            SuggestionDoubleLineViewHolder.LAYOUT -> SuggestionDoubleLineViewHolder(parent, clickListener)
            else -> super.createViewHolder(parent, type)
        }
    }
}