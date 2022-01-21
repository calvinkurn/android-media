package com.tokopedia.autocompletecomponent.suggestion.title

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocompletecomponent.suggestion.SuggestionTypeFactory

class SuggestionTitleDataView(val title: String = "") : Visitable<SuggestionTypeFactory> {

    override fun type(typeFactory: SuggestionTypeFactory): Int {
        return typeFactory.type(this)
    }
}
