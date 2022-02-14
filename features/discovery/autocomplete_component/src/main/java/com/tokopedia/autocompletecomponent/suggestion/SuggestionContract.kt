package com.tokopedia.autocompletecomponent.suggestion

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.autocompletecomponent.suggestion.topshop.SuggestionTopShopCardDataView
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel

interface SuggestionContract {
    interface View : CustomerView {
        fun showSuggestionResult(list: List<Visitable<*>>)

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
            baseSuggestionDataView: BaseSuggestionDataView,
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

        fun route(applink: String, searchParameter: Map<String, String>)

        fun finish()

        val chooseAddressData: LocalCacheModel?
    }

    interface Presenter : CustomerPresenter<View> {
        fun getSearchParameter(): Map<String, String>

        fun getSuggestion(searchParameter: Map<String, String>)

        fun setIsTyping(isTyping: Boolean)

        fun onSuggestionItemClicked(item: BaseSuggestionDataView)

        fun onSuggestionItemImpressed(item: BaseSuggestionDataView)

        fun onTopShopCardClicked(cardData: SuggestionTopShopCardDataView)

        fun onSuggestionChipClicked(
            baseSuggestionDataView: BaseSuggestionDataView,
            item: BaseSuggestionDataView.ChildItem,
        )
    }
}