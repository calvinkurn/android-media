package com.tokopedia.autocompletecomponent.suggestion.doubleline

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocompletecomponent.suggestion.BaseSuggestionDataView
import com.tokopedia.autocompletecomponent.suggestion.SuggestionAdapterTypeFactory
import com.tokopedia.autocompletecomponent.suggestion.TYPE_SHOP

class SuggestionDoubleLineDataDataView : BaseSuggestionDataView(), Visitable<SuggestionAdapterTypeFactory> {

    override fun type(typeFactory: SuggestionAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun isBoldText(): Boolean {
        return type == TYPE_SHOP
    }
}