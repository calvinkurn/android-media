package com.tokopedia.autocompletecomponent.suggestion

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.autocompletecomponent.searchbar.SearchBarKeyword
import com.tokopedia.autocompletecomponent.suggestion.topshop.SuggestionTopShopCardDataView
import com.tokopedia.discovery.common.reimagine.Search1InstAuto
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel

interface SuggestionContract {
    interface View : CustomerView {
        fun showSuggestionResult(list: List<Visitable<*>>)
        fun hideSuggestionResult()

        fun showExceedKeywordLimit()
        fun hideExceedKeywordLimit()

        fun trackEventClickKeyword(
            eventLabel: String,
            dimension90: String,
            baseSuggestionDataView: BaseSuggestionDataView,
        )

        fun trackEventClickCurated(
            eventLabel: String,
            campaignCode: String,
            dimension90: String,
            baseSuggestionDataView: BaseSuggestionDataView,
        )

        fun trackEventClickShop(
            eventLabel: String,
            dimension90: String,
            baseSuggestionDataView: BaseSuggestionDataView,
        )

        fun trackEventClickProfile(
            eventLabel: String,
            baseSuggestionDataView: BaseSuggestionDataView,
        )

        fun trackEventClickRecentKeyword(
            eventLabel: String,
            dimension90: String,
            baseSuggestionDataView: BaseSuggestionDataView,
        )

        fun trackEventClickTopShopCard(eventLabel: String)

        fun trackEventClickTopShopSeeMore(eventLabel: String)

        fun trackEventClickLocalKeyword(
            eventLabel: String,
            userId: String,
            dimension90: String,
            baseSuggestionDataView: BaseSuggestionDataView,
        )

        fun trackEventClickGlobalKeyword(
            eventLabel: String,
            userId: String,
            dimension90: String,
            baseSuggestionDataView: BaseSuggestionDataView,
        )

        fun trackEventClickProductLine(item: BaseSuggestionDataView, eventLabel: String, userId: String)

        fun trackTokoNowEventClickKeyword(
            eventLabel: String,
            baseSuggestionDataView: BaseSuggestionDataView,
        )

        fun trackTokoNowEventClickCurated(
            eventLabel: String,
            baseSuggestionDataView: BaseSuggestionDataView,
        )

        fun trackClickChip(
            eventLabel: String,
            dimension90: String,
            childItem: BaseSuggestionDataView.ChildItem,
        )

        fun trackEventImpressCurated(
            label: String,
            campaignCode: String,
            pageSource: String,
            baseSuggestionDataView: BaseSuggestionDataView,
        )

        fun trackEventImpression(item: BaseSuggestionDataView)

        fun trackEventClick(item: BaseSuggestionDataView)

        fun dropKeyBoard()

        fun route(
            applink: String,
            searchParameter: Map<String, String>,
            activeKeyword: SearchBarKeyword,
            byteIOEnterMethod: String,
        )

        fun applySuggestionToSelectedKeyword(
            suggestedText: String,
            activeKeyword: SearchBarKeyword,
        )

        fun finish()

        fun showSuggestionCoachMark()

        val chooseAddressData: LocalCacheModel?

        val className: String

        fun addToMPSKeyword(item: BaseSuggestionDataView)
    }

    interface Presenter : CustomerPresenter<View> {

        fun isMPS(): Boolean

        fun getSearchParameter(): Map<String, String>

        fun getActiveKeyword() : SearchBarKeyword

        fun getSuggestion(searchParameter: Map<String, String>, activeKeyword: SearchBarKeyword)

        fun setIsTyping(isTyping: Boolean)

        fun onSuggestionItemClicked(item: BaseSuggestionDataView)

        fun onSuggestionItemImpressed(item: BaseSuggestionDataView)

        fun onTopShopCardClicked(cardData: SuggestionTopShopCardDataView)

        fun onSuggestionChipClicked(
            baseSuggestionDataView: BaseSuggestionDataView,
            item: BaseSuggestionDataView.ChildItem,
        )

        fun markSuggestionCoachMark()

    }
}
