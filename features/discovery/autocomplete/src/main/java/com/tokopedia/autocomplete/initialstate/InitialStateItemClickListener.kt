package com.tokopedia.autocomplete.initialstate

import com.tokopedia.autocomplete.initialstate.curatedcampaign.CuratedCampaignViewModel

interface InitialStateItemClickListener {
    fun onDeleteRecentSearchItem(item: BaseItemInitialStateSearch)

    fun onDeleteAllRecentSearch()

    fun onRefreshPopularSearch(featureId: String)

    fun onItemClicked(applink: String, webUrl: String)

    fun onRecentSearchItemClicked(item: BaseItemInitialStateSearch, adapterPosition: Int)

    fun onRecentSearchSeeMoreClicked()

    fun onRefreshDynamicSection(featureId: String)

    fun onDynamicSectionItemClicked(item: BaseItemInitialStateSearch, adapterPosition: Int)

    fun onCuratedCampaignCardClicked(curatedCampaignViewModel: CuratedCampaignViewModel)
}