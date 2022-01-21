package com.tokopedia.autocompletecomponent.suggestion.topshop

interface SuggestionTopShopListener {
    fun onTopShopCardClicked(topShopData: SuggestionTopShopCardDataView)

    fun onTopShopSeeMoreClicked(topShopData: SuggestionTopShopCardDataView)
}