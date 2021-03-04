package com.tokopedia.autocomplete.suggestion

import com.tokopedia.abstraction.base.view.adapter.Visitable

class SuggestionSeparatorViewModel: Visitable<SuggestionTypeFactory> {

    override fun type(typeFactory: SuggestionTypeFactory): Int {
        return typeFactory.type(this)
    }
}