package com.tokopedia.journeydebugger.ui.presenter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

interface JourneyDebugger {
    interface View : CustomerView {

        fun onLoadMoreCompleted(visitables: List<Visitable<*>>)

        fun onReloadCompleted(visitables: List<Visitable<*>>)

        fun onDeleteCompleted()
    }

    interface Presenter : CustomerPresenter<View> {
        fun loadMore()

        fun search(text: String)

        fun reloadData()

        fun deleteAll()
    }
}
