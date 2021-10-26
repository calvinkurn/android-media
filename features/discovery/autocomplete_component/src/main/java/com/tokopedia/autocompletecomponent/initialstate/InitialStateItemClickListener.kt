package com.tokopedia.autocompletecomponent.initialstate

import com.tokopedia.autocompletecomponent.initialstate.curatedcampaign.CuratedCampaignDataView

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

    fun onChipClicked(item: BaseItemInitialStateSearch)
}