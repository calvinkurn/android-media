package com.tokopedia.autocomplete.initialstate

import com.tokopedia.autocomplete.initialstate.curatedcampaign.CuratedCampaignDataView

interface InitialStateItemClickListener {
    fun onDeleteRecentSearchItem(item: BaseItemInitialStateSearch)

    fun onDeleteAllRecentSearch()

    fun onRefreshPopularSearch(featureId: String)

    fun onProductLineClicked(item: BaseItemInitialStateSearch)

    fun onRecentSearchItemClicked(item: BaseItemInitialStateSearch)

    fun onRecentSearchSeeMoreClicked()

    fun onRefreshDynamicSection(featureId: String)

    fun onDynamicSectionItemClicked(item: BaseItemInitialStateSearch)

    fun onCuratedCampaignCardClicked(curatedCampaignDataView: CuratedCampaignDataView)

    fun onRecentViewClicked(item: BaseItemInitialStateSearch)
}