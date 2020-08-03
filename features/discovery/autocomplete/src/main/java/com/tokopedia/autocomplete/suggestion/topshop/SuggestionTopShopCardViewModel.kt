package com.tokopedia.autocomplete.suggestion.topshop

import com.tokopedia.abstraction.base.view.adapter.Visitable

class SuggestionTopShopCardViewModel(
        val type: String = "",
        val id: String = "",
        val applink: String = "",
        val url: String = "",
        val title: String = "",
        val subtitle: String = "",
        val iconTitle: String = "",
        val iconSubtitle: String = "",
        val urlTracker: String = "",
        val imageUrl: String = "",
        val products: List<SuggestionTopShopProductViewModel> = listOf()
): Visitable<SuggestionTopShopAdapterTypeFactory> {
    data class SuggestionTopShopProductViewModel(
        val imageUrl: String = ""
    )

    override fun type(typeFactory: SuggestionTopShopAdapterTypeFactory): Int {
        return typeFactory.type(type)
    }
}