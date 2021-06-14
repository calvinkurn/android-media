package com.tokopedia.autocomplete.suggestion

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.autocomplete.suggestion.topshop.SuggestionTopShopCardDataView

interface SuggestionContract {
    interface View : CustomerView {
        fun showSuggestionResult(list: List<Visitable<*>>)

        fun trackEventClickKeyword(eventLabel: String, dimension90: String)

        fun trackEventClickCurated(eventLabel: String, campaignCode: String, dimension90: String)

        fun trackEventClickShop(eventLabel: String, dimension90: String)

        fun trackEventClickProfile(eventLabel: String)

        fun trackEventClickRecentKeyword(eventLabel: String, dimension90: String)

        fun trackEventClickTopShopCard(eventLabel: String)

        fun trackEventClickTopShopSeeMore(eventLabel: String)

        fun trackEventClickLocalKeyword(eventLabel: String, userId: String, dimension90: String)

        fun trackEventClickGlobalKeyword(eventLabel: String, userId: String, dimension90: String)

        fun dropKeyBoard()

        fun route(applink: String, searchParameter: Map<String, String>)

        fun finish()
    }

    interface Presenter : CustomerPresenter<View> {
        fun search()

        fun onSuggestionItemClicked(item: BaseSuggestionDataView)

        fun onTopShopCardClicked(cardData: SuggestionTopShopCardDataView)
    }
}