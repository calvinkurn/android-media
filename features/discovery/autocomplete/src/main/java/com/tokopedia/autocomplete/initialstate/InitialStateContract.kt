package com.tokopedia.autocomplete.initialstate

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.discovery.common.model.SearchParameter

interface InitialStateContract {
    interface View : CustomerView {
        fun showInitialStateResult(initialStateVisitableList: List<Visitable<*>>)

        fun refreshPopularSearch(list: List<Visitable<*>>)

        fun deleteRecentSearch(list: List<Visitable<*>>)
    }

    interface Presenter : CustomerPresenter<View> {
        fun getInitialStateData()

        fun deleteRecentSearchItem(keyword: String)

        fun deleteAllRecentSearch()

        fun refreshPopularSearch()
    }
}