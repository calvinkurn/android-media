package com.tokopedia.analyticsdebugger.debugger.ui

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

/**
 * @author okasurya on 5/16/18.
 */
interface AnalyticsDebugger {
    interface View : CustomerView {
        fun onLoadMoreCompleted(visitables: List<Visitable<*>>)

        fun onReloadCompleted(visitables: List<Visitable<*>>)

        fun onDeleteCompleted()

        fun showCount(count: Int)
    }

    interface Presenter : CustomerPresenter<View> {
        fun loadMore()

        fun search(text: String)

        fun reloadData()

        fun deleteAll()
    }
}
