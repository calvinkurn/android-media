package com.tokopedia.autocomplete.suggestion

import com.tokopedia.abstraction.base.view.adapter.Visitable

class SuggestionTitleViewModel(val title: String = "") : Visitable<SuggestionTypeFactory> {

    override fun type(typeFactory: SuggestionTypeFactory): Int {
        return typeFactory.type(this)
    }
}
