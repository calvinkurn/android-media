package com.tokopedia.autocompletecomponent.suggestion.doubleline

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocompletecomponent.suggestion.*

class SuggestionDoubleLineDataDataView(
    val data: BaseSuggestionDataView = BaseSuggestionDataView()
) : Visitable<SuggestionAdapterTypeFactory> {

    override fun type(typeFactory: SuggestionAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}