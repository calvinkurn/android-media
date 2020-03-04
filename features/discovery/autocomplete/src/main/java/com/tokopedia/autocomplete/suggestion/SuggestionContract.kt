package com.tokopedia.autocomplete.suggestion

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.discovery.common.model.SearchParameter

interface SuggestionContract {
    interface View : CustomerView {
        fun showSuggestionResult(list: MutableList<Visitable<*>>)
    }

    interface Presenter : CustomerPresenter<View> {
        fun search(searchParameter: SearchParameter)

        fun onItemClicked(item: BaseSuggestionViewModel)
    }
}