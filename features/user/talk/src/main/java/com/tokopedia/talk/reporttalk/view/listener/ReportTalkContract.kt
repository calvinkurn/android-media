package com.tokopedia.talk.reporttalk.view.listener

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

/**
 * @author by nisie on 8/30/18.
 */
interface ReportTalkContract {

    interface View : CustomerView {
        fun showLoadingFull()

    }

    interface Presenter : CustomerPresenter<View> {

    }
}