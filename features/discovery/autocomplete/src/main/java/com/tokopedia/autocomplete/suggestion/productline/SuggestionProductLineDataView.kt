package com.tokopedia.autocomplete.suggestion.productline

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocomplete.suggestion.BaseSuggestionViewModel
import com.tokopedia.autocomplete.suggestion.SuggestionAdapterTypeFactory

class SuggestionProductLineDataView : BaseSuggestionViewModel(), Visitable<SuggestionAdapterTypeFactory> {

    override fun type(typeFactory: SuggestionAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}