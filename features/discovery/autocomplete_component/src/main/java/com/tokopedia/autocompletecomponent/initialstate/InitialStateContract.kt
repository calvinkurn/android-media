package com.tokopedia.autocompletecomponent.initialstate

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.autocompletecomponent.initialstate.curatedcampaign.CuratedCampaignDataView
import com.tokopedia.autocompletecomponent.initialstate.dynamic.DynamicInitialStateItemTrackingModel
import com.tokopedia.autocompletecomponent.initialstate.dynamic.DynamicInitialStateSearchDataView
import com.tokopedia.autocompletecomponent.initialstate.popularsearch.PopularSearchDataView
import com.tokopedia.autocompletecomponent.initialstate.recentsearch.RecentSearchDataView
import com.tokopedia.autocompletecomponent.initialstate.recentview.RecentViewDataView
import com.tokopedia.discovery.common.reimagine.Search1InstAuto
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel

interface InitialStateContract {
    interface View : CustomerView {
        fun showInitialStateResult(initialStateVisitableList: List<Visitable<*>>)

        fun onRecentViewImpressed(recentViewDataView: RecentViewDataView, list: List<Any>)

        fun onRecentSearchImpressed(
            recentSearchList: List<BaseItemInitialStateSearch>,
            list: List<Any>
        )

        fun onPopularSearchImpressed(
            popularSearchDataView: PopularSearchDataView,
            model: DynamicInitialStateItemTrackingModel
        )

        fun onSeeMoreRecentSearchImpressed(userId: String)

        fun route(applink: String, searchParameter: Map<String, String>)

        fun finish()

        fun trackEventClickRecentSearch(item: BaseItemInitialStateSearch, label: String)

        fun trackEventClickRecentShop(item: BaseItemInitialStateSearch, label: String, userId: String)

        fun trackEventClickSeeMoreRecentSearch(userId: String)

        fun renderCompleteRecentSearch(recentSearchDataView: RecentSearchDataView)

        fun dropKeyBoard()

        fun onDynamicSectionImpressed(
            dynamicInitialStateSearchDataView: DynamicInitialStateSearchDataView,
            model: DynamicInitialStateItemTrackingModel,
        )

        fun trackEventClickDynamicSectionItem(
            userId: String,
            label: String,
            item: BaseItemInitialStateSearch,
            type: String,
            pageSource: String
        )

        fun refreshViewWithPosition(position: Int)

        fun trackEventClickCuratedCampaignCard(
            userId: String,
            label: String,
            item: BaseItemInitialStateSearch,
            type: String,
            campaignCode: String
        )

        fun onCuratedCampaignCardImpressed(
            userId: String,
            label: String,
            item: BaseItemInitialStateSearch,
            type: String,
            campaignCode: String
        )

        fun trackEventClickRecentView(item: BaseItemInitialStateSearch, label: String)

        fun trackEventClickProductLine(item: BaseItemInitialStateSearch, userId: String, label: String)

        val chooseAddressData: LocalCacheModel?

        fun onRefreshPopularSearch()

        fun onRefreshTokoNowPopularSearch()

        fun trackEventClickTokoNowDynamicSectionItem(label: String, item: BaseItemInitialStateSearch)

        fun trackEventClickChip(
            userId: String,
            label: String,
            item: BaseItemInitialStateSearch,
            type: String,
            pageSource: String
        )

        fun trackEventClickSearchBarEducation(item: BaseItemInitialStateSearch)

        fun enableMps()
        fun disableMps()
    }

    interface ReimagineRollance {
        fun getVariantReimagineRollance(): Search1InstAuto
    }

    interface Presenter : CustomerPresenter<View> {
        val recentSearchPosition: Int

        val seeMoreButtonPosition: Int

        fun getSearchParameter(): Map<String, String>

        fun showInitialState(searchParameter: Map<String, String>)

        fun deleteRecentSearchItem(item: BaseItemInitialStateSearch)

        fun deleteAllRecentSearch()

        fun refreshPopularSearch(featureId: String)

        fun refreshDynamicSection(featureId: String)

        fun getQueryKey(): String

        fun onRecentSearchItemClicked(item: BaseItemInitialStateSearch)

        fun recentSearchSeeMoreClicked()

        fun onDynamicSectionItemClicked(item: BaseItemInitialStateSearch)

        fun onCuratedCampaignCardClicked(curatedCampaignDataView: CuratedCampaignDataView)

        fun onRecentViewClicked(item: BaseItemInitialStateSearch)

        fun onProductLineClicked(item: BaseItemInitialStateSearch)

        fun onChipClicked(item: BaseItemInitialStateSearch)

        fun onSearchBarEducationClick(item: BaseItemInitialStateSearch)
    }
}
