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
import com.tokopedia.autocomplete.suggestion.topshop.SuggestionTopShopListener
import com.tokopedia.autocomplete.suggestion.topshop.SuggestionTopShopWidgetViewHolder
import com.tokopedia.autocomplete.suggestion.topshop.SuggestionTopShopWidgetViewModel

class SuggestionAdapterTypeFactory(
        private val suggestionClickListener: SuggestionClickListener,
        private val suggestionTopShopListener: SuggestionTopShopListener
) : BaseAdapterTypeFactory(), SuggestionTypeFactory {

    override fun type(viewModel: SuggestionTitleViewModel): Int {
        return SuggestionTitleViewHolder.LAYOUT
    }

    override fun type(viewModel: SuggestionSingleLineViewModel): Int {
        return SuggestionSingleLineViewHolder.LAYOUT
    }

    override fun type(viewModel: SuggestionDoubleLineViewModel): Int {
        return SuggestionDoubleLineViewHolder.LAYOUT
    }

    override fun type(viewModel: SuggestionTopShopWidgetViewModel): Int {
        return SuggestionTopShopWidgetViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            SuggestionTitleViewHolder.LAYOUT -> SuggestionTitleViewHolder(parent)
            SuggestionSingleLineViewHolder.LAYOUT -> SuggestionSingleLineViewHolder(parent, suggestionClickListener)
            SuggestionDoubleLineViewHolder.LAYOUT -> SuggestionDoubleLineViewHolder(parent, suggestionClickListener)
            SuggestionTopShopWidgetViewHolder.LAYOUT -> SuggestionTopShopWidgetViewHolder(parent, suggestionTopShopListener)
            else -> super.createViewHolder(parent, type)
        }
    }
}