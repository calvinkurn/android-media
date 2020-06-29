package com.tokopedia.autocomplete.suggestion.topshop

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocomplete.suggestion.SuggestionAdapterTypeFactory

data class SuggestionTopShopWidgetViewModel(
        var template: String = "",
        var title: String = "",
        var position: Int = -1,
        var listSuggestionTopShopCard: List<SuggestionTopShopCardViewModel> = listOf()
): Visitable<SuggestionAdapterTypeFactory> {

    override fun type(typeFactory: SuggestionAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}