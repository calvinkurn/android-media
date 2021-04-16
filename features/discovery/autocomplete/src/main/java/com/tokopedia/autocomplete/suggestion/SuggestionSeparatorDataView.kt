package com.tokopedia.autocomplete.suggestion

import com.tokopedia.abstraction.base.view.adapter.Visitable

class SuggestionSeparatorDataView: Visitable<SuggestionTypeFactory> {

    override fun type(typeFactory: SuggestionTypeFactory): Int {
        return typeFactory.type(this)
    }
}