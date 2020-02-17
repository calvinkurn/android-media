package com.tokopedia.autocomplete.suggestion

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.discovery.common.model.SearchParameter

interface SuggestionContract {
    interface View : CustomerView {
        fun showSuggestionResult(allFragmentList: MutableList<Visitable<*>>, productFragmentList: MutableList<Visitable<*>>, shopFragmentList: MutableList<Visitable<*>>)
    }

    interface Presenter : CustomerPresenter<View> {
        fun search(searchParameter: SearchParameter)
    }
}