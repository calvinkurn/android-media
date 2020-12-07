package com.tokopedia.autocomplete.initialstate

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.autocomplete.initialstate.curatedcampaign.CuratedCampaignViewModel
import com.tokopedia.autocomplete.initialstate.dynamic.DynamicInitialStateItemTrackingModel
import com.tokopedia.autocomplete.initialstate.recentsearch.RecentSearchViewModel

interface InitialStateContract {
    interface View : CustomerView {
        fun showInitialStateResult(initialStateVisitableList: List<Visitable<*>>)

        fun onRecentViewImpressed(list: List<Any>)

        fun onRecentSearchImpressed(list: List<Any>)

        fun onPopularSearchImpressed(model: DynamicInitialStateItemTrackingModel)

        fun onSeeMoreRecentSearchImpressed(userId: String)

        fun route(applink: String, searchParameter: Map<String, String>)

        fun finish()

        fun trackEventClickRecentSearch(label: String, adapterPosition: Int)

        fun trackEventClickRecentShop(label: String, userId: String)

        fun trackEventClickSeeMoreRecentSearch(userId: String)

        fun renderCompleteRecentSearch(recentSearchViewModel: RecentSearchViewModel)

        fun dropKeyBoard()

        fun onDynamicSectionImpressed(model: DynamicInitialStateItemTrackingModel)

        fun trackEventClickDynamicSectionItem(userId: String, label: String, type: String)

        fun refreshViewWithPosition(position: Int)

        fun trackEventClickCuratedCampaignCard(userId: String, label: String, type: String)

        fun onCuratedCampaignCardImpressed(userId: String, label: String, type: String)
    }

    interface Presenter : CustomerPresenter<View> {
        fun getInitialStateData()

        fun deleteRecentSearchItem(item: BaseItemInitialStateSearch)

        fun deleteAllRecentSearch()

        fun refreshPopularSearch(featureId: String)

        fun refreshDynamicSection(featureId: String)

        fun getQueryKey(): String

        fun onRecentSearchItemClicked(item: BaseItemInitialStateSearch, adapterPosition: Int)

        fun recentSearchSeeMoreClicked()

        fun onDynamicSectionItemClicked(item: BaseItemInitialStateSearch, adapterPosition: Int)

        fun onCuratedCampaignCardClicked(curatedCampaignViewModel: CuratedCampaignViewModel)
    }
}