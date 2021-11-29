package com.tokopedia.autocompletecomponent.suggestion.separator

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocompletecomponent.suggestion.SuggestionTypeFactory

class SuggestionSeparatorDataView: Visitable<SuggestionTypeFactory> {

    override fun type(typeFactory: SuggestionTypeFactory): Int {
        return typeFactory.type(this)
    }
}