package com.tokopedia.autocompletecomponent.suggestion

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocompletecomponent.suggestion.chips.SuggestionChipWidgetDataView
import com.tokopedia.autocompletecomponent.suggestion.productline.SuggestionProductLineDataDataView
import com.tokopedia.autocompletecomponent.suggestion.doubleline.SuggestionDoubleLineDataDataView
import com.tokopedia.autocompletecomponent.suggestion.doubleline.SuggestionDoubleLineWithoutImageDataDataView
import com.tokopedia.autocompletecomponent.suggestion.separator.SuggestionSeparatorDataView
import com.tokopedia.autocompletecomponent.suggestion.singleline.SuggestionSingleLineDataDataView
import com.tokopedia.autocompletecomponent.suggestion.title.SuggestionTitleDataView
import com.tokopedia.autocompletecomponent.suggestion.topshop.SuggestionTopShopWidgetDataView

interface SuggestionTypeFactory {

    fun type(suggestionTitleDataView: SuggestionTitleDataView): Int

    fun type(suggestionSingleLineDataView: SuggestionSingleLineDataDataView): Int

    fun type(suggestionDoubleLineDataView: SuggestionDoubleLineDataDataView): Int

    fun type(suggestionTopShopWidgetDataView: SuggestionTopShopWidgetDataView): Int

    fun type(suggestionDoubleLineWithoutImageDataView: SuggestionDoubleLineWithoutImageDataDataView): Int

    fun type(suggestionSeparatorDataView: SuggestionSeparatorDataView): Int

    fun type(suggestionProductLineDataView: SuggestionProductLineDataDataView): Int

    fun type(suggestionChipWidgetDataView: SuggestionChipWidgetDataView): Int

    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>
}