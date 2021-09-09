package com.tokopedia.analyticsdebugger.debugger.ui.presenter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

interface SharedPrefInterface {
    interface View : CustomerView {
        fun onReloadCompleted(visitables: List<Visitable<*>>)
    }
}
