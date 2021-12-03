package com.tokopedia.autocompletecomponent.suggestion.doubleline

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocompletecomponent.suggestion.BaseSuggestionDataView
import com.tokopedia.autocompletecomponent.suggestion.SuggestionAdapterTypeFactory

class SuggestionDoubleLineWithoutImageDataDataView(
    val data: BaseSuggestionDataView = BaseSuggestionDataView()
) : Visitable<SuggestionAdapterTypeFactory> {

    override fun type(typeFactory: SuggestionAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}