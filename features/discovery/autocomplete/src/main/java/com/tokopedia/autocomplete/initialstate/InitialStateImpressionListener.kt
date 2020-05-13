package com.tokopedia.autocomplete.initialstate

interface InitialStateImpressionListener {

    fun onRecentViewImpressed(list: List<BaseItemInitialStateSearch>)

    fun onRecentSearchImpressed(list: List<BaseItemInitialStateSearch>)

    fun onPopularSearchImpressed(list: List<BaseItemInitialStateSearch>)
}