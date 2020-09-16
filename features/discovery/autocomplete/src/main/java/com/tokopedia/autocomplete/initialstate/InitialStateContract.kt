package com.tokopedia.autocomplete.initialstate

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

interface InitialStateContract {
    interface View : CustomerView {
        fun showInitialStateResult(initialStateVisitableList: List<Visitable<*>>)

        fun refreshPopularSearch(list: List<Visitable<*>>)

        fun deleteRecentSearch(list: List<Visitable<*>>)

        fun onRecentViewImpressed(list: List<Any>)

        fun onRecentSearchImpressed(list: List<Any>)

        fun onPopularSearchImpressed(list: List<Any>)

        fun route(applink: String, searchParameter: Map<String, String>)

        fun finish()

        fun trackEventClickRecentSearch(label: String, adapterPosition: Int)

        fun trackEventClickRecentShop(label: String, userId: String)

        fun renderRecentSearch()

        fun dropKeyBoard()
    }

    interface Presenter : CustomerPresenter<View> {
        fun getInitialStateData()

        fun deleteRecentSearchItem(item: BaseItemInitialStateSearch)

        fun deleteAllRecentSearch()

        fun refreshPopularSearch()

        fun getQueryKey(): String

        fun onRecentSearchItemClicked(item: BaseItemInitialStateSearch, adapterPosition: Int)

        fun recentSearchSeeMoreClicked()
    }
}