package com.tokopedia.autocomplete.suggestion.topshop

import com.tokopedia.autocomplete.suggestion.topshop.SuggestionTopShopCardViewModel

interface SuggestionTopShopListener {
    fun onTopShopCardClicked(topShop: SuggestionTopShopCardViewModel)

    fun onTopShopSeeMoreClicked(topShop: SuggestionTopShopCardViewModel)
}