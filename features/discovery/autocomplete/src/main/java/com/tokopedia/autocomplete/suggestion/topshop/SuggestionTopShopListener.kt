package com.tokopedia.autocomplete.suggestion.topshop

interface SuggestionTopShopListener {
    fun onTopShopCardClicked(topShopData: SuggestionTopShopCardDataView)

    fun onTopShopSeeMoreClicked(topShopData: SuggestionTopShopCardDataView)
}