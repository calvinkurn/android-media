package com.tokopedia.autocomplete.initialstate

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.autocomplete.InitialStateViewModel
import com.tokopedia.autocomplete.domain.model.SearchData
import com.tokopedia.discovery.common.model.SearchParameter

interface InitialStateContract {
    interface View : CustomerView {
        fun showInitialStateResult(initialStateViewModel: InitialStateViewModel)
    }

    interface Presenter : CustomerPresenter<View> {
        fun search(searchParameter: SearchParameter)

        fun deleteRecentSearchItem(keyword: String)

        fun deleteAllRecentSearch()

        fun getInitialStateResult(list: MutableList<SearchData>, searchTerm: String): List<Visitable<*>>
    }
}