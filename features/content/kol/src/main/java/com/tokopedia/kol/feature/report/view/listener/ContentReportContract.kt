package com.tokopedia.kol.feature.report.view.listener

import android.content.Context
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

/**
 * @author by milhamj on 08/11/18.
 */
interface ContentReportContract {
    interface View : CustomerView {
        fun getContext(): Context?

        fun hideKeyboard()

        fun enableSendBtn()

        fun showLoading()

        fun hideLoading()
    }

    interface Presenter : CustomerPresenter<View> {
        fun sendReport(contentId: Int, reasonType: String, reasonMessage: String)
    }
}