package com.tokopedia.autocompletecomponent.suggestion.topshop

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocompletecomponent.suggestion.SuggestionAdapterTypeFactory

data class SuggestionTopShopWidgetDataView(
        var template: String = "",
        var title: String = "",
        var position: Int = -1,
        var listSuggestionTopShopCardData: List<SuggestionTopShopCardDataView> = listOf()
): Visitable<SuggestionAdapterTypeFactory> {

    override fun type(typeFactory: SuggestionAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}