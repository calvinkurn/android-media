package com.tokopedia.autocomplete.suggestion

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocomplete.suggestion.productline.SuggestionProductLineDataDataView
import com.tokopedia.autocomplete.suggestion.doubleline.SuggestionDoubleLineDataDataView
import com.tokopedia.autocomplete.suggestion.doubleline.SuggestionDoubleLineWithoutImageDataDataView
import com.tokopedia.autocomplete.suggestion.singleline.SuggestionSingleLineDataDataView
import com.tokopedia.autocomplete.suggestion.title.SuggestionTitleDataView
import com.tokopedia.autocomplete.suggestion.topshop.SuggestionTopShopWidgetDataView

interface SuggestionTypeFactory {

    fun type(suggestionTitleDataView: SuggestionTitleDataView): Int

    fun type(suggestionSingleLineDataView: SuggestionSingleLineDataDataView): Int

    fun type(suggestionDoubleLineDataView: SuggestionDoubleLineDataDataView): Int

    fun type(suggestionTopShopWidgetDataView: SuggestionTopShopWidgetDataView): Int

    fun type(suggestionDoubleLineWithoutImageDataView: SuggestionDoubleLineWithoutImageDataDataView): Int

    fun type(suggestionSeparatorDataView: SuggestionSeparatorDataView): Int

    fun type(suggestionProductLineDataView: SuggestionProductLineDataDataView): Int

    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>
}