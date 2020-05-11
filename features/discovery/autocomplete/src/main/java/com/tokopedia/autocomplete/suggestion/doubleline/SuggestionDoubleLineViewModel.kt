package com.tokopedia.autocomplete.suggestion.doubleline

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocomplete.suggestion.BaseSuggestionViewModel
import com.tokopedia.autocomplete.suggestion.SuggestionAdapterTypeFactory

class SuggestionDoubleLineViewModel : BaseSuggestionViewModel(), Visitable<SuggestionAdapterTypeFactory> {

    override fun type(typeFactory: SuggestionAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}