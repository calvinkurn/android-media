package com.tokopedia.autocomplete.suggestion

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocomplete.suggestion.productline.SuggestionProductLineViewHolder
import com.tokopedia.autocomplete.suggestion.productline.SuggestionProductLineDataDataView
import com.tokopedia.autocomplete.suggestion.doubleline.SuggestionDoubleLineViewHolder
import com.tokopedia.autocomplete.suggestion.doubleline.SuggestionDoubleLineDataDataView
import com.tokopedia.autocomplete.suggestion.doubleline.SuggestionDoubleLineWithoutImageViewHolder
import com.tokopedia.autocomplete.suggestion.doubleline.SuggestionDoubleLineWithoutImageDataDataView
import com.tokopedia.autocomplete.suggestion.singleline.SuggestionSingleLineViewHolder
import com.tokopedia.autocomplete.suggestion.singleline.SuggestionSingleLineDataDataView
import com.tokopedia.autocomplete.suggestion.title.SuggestionTitleViewHolder
import com.tokopedia.autocomplete.suggestion.title.SuggestionTitleDataView
import com.tokopedia.autocomplete.suggestion.topshop.SuggestionTopShopListener
import com.tokopedia.autocomplete.suggestion.topshop.SuggestionTopShopWidgetViewHolder
import com.tokopedia.autocomplete.suggestion.topshop.SuggestionTopShopWidgetDataView

class SuggestionAdapterTypeFactory(
        private val suggestionClickListener: SuggestionClickListener,
        private val suggestionTopShopListener: SuggestionTopShopListener
) : BaseAdapterTypeFactory(), SuggestionTypeFactory {

    override fun type(suggestionTitleDataView: SuggestionTitleDataView): Int {
        return SuggestionTitleViewHolder.LAYOUT
    }

    override fun type(suggestionSingleLineDataView: SuggestionSingleLineDataDataView): Int {
        return SuggestionSingleLineViewHolder.LAYOUT
    }

    override fun type(suggestionDoubleLineDataView: SuggestionDoubleLineDataDataView): Int {
        return SuggestionDoubleLineViewHolder.LAYOUT
    }

    override fun type(suggestionTopShopWidgetDataView: SuggestionTopShopWidgetDataView): Int {
        return SuggestionTopShopWidgetViewHolder.LAYOUT
    }

    override fun type(suggestionDoubleLineWithoutImageDataView: SuggestionDoubleLineWithoutImageDataDataView): Int {
        return SuggestionDoubleLineWithoutImageViewHolder.LAYOUT
    }

    override fun type(suggestionSeparatorDataView: SuggestionSeparatorDataView): Int {
        return SuggestionSeparatorViewHolder.LAYOUT
    }

    override fun type(suggestionProductLineDataView: SuggestionProductLineDataDataView): Int {
        return SuggestionProductLineViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            SuggestionTitleViewHolder.LAYOUT -> SuggestionTitleViewHolder(parent)
            SuggestionSingleLineViewHolder.LAYOUT -> SuggestionSingleLineViewHolder(parent, suggestionClickListener)
            SuggestionDoubleLineViewHolder.LAYOUT -> SuggestionDoubleLineViewHolder(parent, suggestionClickListener)
            SuggestionTopShopWidgetViewHolder.LAYOUT -> SuggestionTopShopWidgetViewHolder(parent, suggestionTopShopListener)
            SuggestionDoubleLineWithoutImageViewHolder.LAYOUT -> SuggestionDoubleLineWithoutImageViewHolder(parent, suggestionClickListener)
            SuggestionSeparatorViewHolder.LAYOUT -> SuggestionSeparatorViewHolder(parent)
            SuggestionProductLineViewHolder.LAYOUT -> SuggestionProductLineViewHolder(parent, suggestionClickListener)
            else -> super.createViewHolder(parent, type)
        }
    }
}