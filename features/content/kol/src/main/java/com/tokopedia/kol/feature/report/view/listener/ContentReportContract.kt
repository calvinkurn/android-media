package com.tokopedia.kol.feature.report.view.listener

import android.content.Context
import androidx.annotation.StringRes
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

/**
 * @author by milhamj on 08/11/18.
 */
interface ContentReportContract {
    interface View : CustomerView {
        fun getContext(): Context?

        fun getString(@StringRes id: Int): String

        fun hideKeyboard()

        fun enableSendBtn()

        fun showLoading()

        fun hideLoading()

        fun onSuccessSendReport()

        fun onErrorSendReport(message: String)

        fun onErrorSendReportDuplicate(message: String)
    }

    interface Presenter : CustomerPresenter<View> {
        fun sendReport(contentId: Int, reasonType: String, reasonMessage: String,contentType:String)
    }
}