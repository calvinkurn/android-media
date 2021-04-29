package com.tokopedia.autocomplete.suggestion.singleline

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocomplete.suggestion.BaseSuggestionDataView
import com.tokopedia.autocomplete.suggestion.SuggestionAdapterTypeFactory

class SuggestionSingleLineDataDataView : BaseSuggestionDataView(), Visitable<SuggestionAdapterTypeFactory> {

    override fun type(typeFactory: SuggestionAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}