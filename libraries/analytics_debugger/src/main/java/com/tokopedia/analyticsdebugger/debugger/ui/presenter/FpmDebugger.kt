package com.tokopedia.analyticsdebugger.debugger.ui.presenter

import android.content.Context
import android.net.Uri

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

interface FpmDebugger {
    interface View : CustomerView {

        fun getViewContext(): Context

        fun onLoadMoreCompleted(visitables: List<Visitable<*>>)

        fun onReloadCompleted(visitables: List<Visitable<*>>)

        fun onDeleteCompleted()
    }

    interface Presenter : CustomerPresenter<View> {
        fun loadMore()

        fun search(text: String)

        fun reloadData()

        fun deleteAll()

        fun writeAllDataToFile(fileUri: Uri)
    }
}
