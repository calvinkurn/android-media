package com.tokopedia.autocompletecomponent.suggestion

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocompletecomponent.suggestion.chips.SuggestionChipListener
import com.tokopedia.autocompletecomponent.suggestion.chips.SuggestionChipWidgetDataView
import com.tokopedia.autocompletecomponent.suggestion.chips.SuggestionChipWidgetViewHolder
import com.tokopedia.autocompletecomponent.suggestion.productline.SuggestionProductLineViewHolder
import com.tokopedia.autocompletecomponent.suggestion.productline.SuggestionProductLineDataDataView
import com.tokopedia.autocompletecomponent.suggestion.doubleline.SuggestionDoubleLineViewHolder
import com.tokopedia.autocompletecomponent.suggestion.doubleline.SuggestionDoubleLineDataDataView
import com.tokopedia.autocompletecomponent.suggestion.doubleline.SuggestionDoubleLineWithoutImageViewHolder
import com.tokopedia.autocompletecomponent.suggestion.doubleline.SuggestionDoubleLineWithoutImageDataDataView
import com.tokopedia.autocompletecomponent.suggestion.separator.SuggestionSeparatorDataView
import com.tokopedia.autocompletecomponent.suggestion.separator.SuggestionSeparatorViewHolder
import com.tokopedia.autocompletecomponent.suggestion.singleline.SuggestionSingleLineViewHolder
import com.tokopedia.autocompletecomponent.suggestion.singleline.SuggestionSingleLineDataDataView
import com.tokopedia.autocompletecomponent.suggestion.title.SuggestionTitleViewHolder
import com.tokopedia.autocompletecomponent.suggestion.title.SuggestionTitleDataView
import com.tokopedia.autocompletecomponent.suggestion.topshop.SuggestionTopShopListener
import com.tokopedia.autocompletecomponent.suggestion.topshop.SuggestionTopShopWidgetViewHolder
import com.tokopedia.autocompletecomponent.suggestion.topshop.SuggestionTopShopWidgetDataView
import com.tokopedia.discovery.common.reimagine.Search1InstAuto

class SuggestionAdapterTypeFactory(
    private val suggestionListener: SuggestionListener,
    private val suggestionTopShopListener: SuggestionTopShopListener,
    private val suggestionChipListener: SuggestionChipListener,
    private val reimagineVariant: Search1InstAuto
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
            SuggestionDoubleLineViewHolder.LAYOUT -> SuggestionDoubleLineViewHolder(parent, suggestionListener, reimagineVariant)
            SuggestionTopShopWidgetViewHolder.LAYOUT -> SuggestionTopShopWidgetViewHolder(parent, suggestionTopShopListener)
            SuggestionDoubleLineWithoutImageViewHolder.LAYOUT -> SuggestionDoubleLineWithoutImageViewHolder(parent, suggestionListener)
            SuggestionSeparatorViewHolder.LAYOUT -> SuggestionSeparatorViewHolder(parent)
            SuggestionProductLineViewHolder.LAYOUT -> SuggestionProductLineViewHolder(parent, suggestionListener)
            SuggestionChipWidgetViewHolder.LAYOUT ->
                SuggestionChipWidgetViewHolder(parent, suggestionChipListener)
            else -> super.createViewHolder(parent, type)
        }
    }
}
