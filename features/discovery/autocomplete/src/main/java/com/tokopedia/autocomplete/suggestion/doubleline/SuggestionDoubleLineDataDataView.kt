package com.tokopedia.autocomplete.suggestion.doubleline

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocomplete.suggestion.BaseSuggestionDataView
import com.tokopedia.autocomplete.suggestion.SuggestionAdapterTypeFactory
import com.tokopedia.autocomplete.suggestion.TYPE_SHOP

class SuggestionDoubleLineDataDataView : BaseSuggestionDataView(), Visitable<SuggestionAdapterTypeFactory> {

    override fun type(typeFactory: SuggestionAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun isBoldText(): Boolean {
        return type == TYPE_SHOP
    }
}