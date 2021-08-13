package com.tokopedia.autocomplete.suggestion

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocomplete.suggestion.chips.SuggestionChipWidgetDataView
import com.tokopedia.autocomplete.suggestion.chips.SuggestionChipWidgetViewHolder
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
    private val suggestionListener: SuggestionListener,
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

    override fun type(suggestionChipWidgetDataView: SuggestionChipWidgetDataView): Int {
        return SuggestionChipWidgetViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            SuggestionTitleViewHolder.LAYOUT -> SuggestionTitleViewHolder(parent)
            SuggestionSingleLineViewHolder.LAYOUT -> SuggestionSingleLineViewHolder(parent, suggestionListener)
            SuggestionDoubleLineViewHolder.LAYOUT -> SuggestionDoubleLineViewHolder(parent, suggestionListener)
            SuggestionTopShopWidgetViewHolder.LAYOUT -> SuggestionTopShopWidgetViewHolder(parent, suggestionTopShopListener)
            SuggestionDoubleLineWithoutImageViewHolder.LAYOUT -> SuggestionDoubleLineWithoutImageViewHolder(parent, suggestionListener)
            SuggestionSeparatorViewHolder.LAYOUT -> SuggestionSeparatorViewHolder(parent)
            SuggestionProductLineViewHolder.LAYOUT -> SuggestionProductLineViewHolder(parent, suggestionListener)
            SuggestionChipWidgetViewHolder.LAYOUT -> SuggestionChipWidgetViewHolder(parent, suggestionListener)
            else -> super.createViewHolder(parent, type)
        }
    }
}