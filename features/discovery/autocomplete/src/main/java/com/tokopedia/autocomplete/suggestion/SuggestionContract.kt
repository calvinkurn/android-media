package com.tokopedia.autocomplete.suggestion

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.discovery.common.model.SearchParameter

interface SuggestionContract {
    interface View : CustomerView {
        fun showSuggestionResult(list: MutableList<Visitable<*>>)

        fun trackEventClickKeyword(eventLabel: String)

        fun trackEventClickCurated(eventLabel: String, campaignCode: String)

        fun trackEventClickShop(eventLabel: String)

        fun trackEventClickProfile(eventLabel: String)

        fun trackEventClickRecentKeyword(eventLabel: String)

        fun dropKeyBoard()

        fun route(applink: String)

        fun finish()
    }

    interface Presenter : CustomerPresenter<View> {
        fun search(searchParameter: SearchParameter)

        fun onSuggestionItemClicked(item: BaseSuggestionViewModel)
    }
}