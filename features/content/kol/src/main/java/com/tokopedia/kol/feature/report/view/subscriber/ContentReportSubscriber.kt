package com.tokopedia.kol.feature.report.view.subscriber

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.kol.R
import com.tokopedia.kol.common.util.debugTrace
import com.tokopedia.kol.feature.report.view.listener.ContentReportContract
import rx.Subscriber

/**
 * @author by milhamj on 15/11/18.
 */
class ContentReportSubscriber(val view: ContentReportContract.View)
     : Subscriber<Boolean>() {
    override fun onNext(isSuccess: Boolean) {
        if (isSuccess) {
            view.onSuccessSendReport()
        } else {
            onError(MessageErrorException(view.getContext()!!.getString(R.string.kol_report_error)))
        }
    }

    override fun onCompleted() {
    }

    override fun onError(e: Throwable?) {
        e?.debugTrace()

        val errorMsg = ErrorHandler.getErrorMessage(view.getContext(), e)
        if (errorMsg.contains(view.getString(R.string.kol_duplicate), ignoreCase = true)) {
            view.onErrorSendReportDuplicate(errorMsg)
        } else {
            view.hideLoading()
            view.onErrorSendReport(errorMsg)
        }
    }
}