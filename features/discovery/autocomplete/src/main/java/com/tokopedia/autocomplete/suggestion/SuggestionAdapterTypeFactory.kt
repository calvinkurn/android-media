package com.tokopedia.autocomplete.suggestion

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

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