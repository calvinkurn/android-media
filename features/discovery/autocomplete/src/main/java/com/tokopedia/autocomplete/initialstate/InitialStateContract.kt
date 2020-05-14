package com.tokopedia.autocomplete.initialstate

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

interface InitialStateContract {
    interface View : CustomerView {
        fun showInitialStateResult(initialStateVisitableList: List<Visitable<*>>)

        fun refreshPopularSearch(list: List<Visitable<*>>)

        fun deleteRecentSearch(list: List<Visitable<*>>)

        fun onRecentViewImpressed(list: MutableList<Any>)

        fun onRecentSearchImpressed(list: MutableList<Any>)

        fun onPopularSearchImpressed(list: MutableList<Any>)
    }

    interface Presenter : CustomerPresenter<View> {
        fun getInitialStateData()

        fun deleteRecentSearchItem(keyword: String)

        fun deleteAllRecentSearch()

        fun refreshPopularSearch()

        fun getQueryKey(): String
    }
}