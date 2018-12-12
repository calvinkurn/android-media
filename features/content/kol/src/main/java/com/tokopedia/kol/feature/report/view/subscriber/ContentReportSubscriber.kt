package com.tokopedia.kol.feature.report.view.subscriber

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.kol.R
import com.tokopedia.kol.feature.report.data.entity.SendReportResponse
import com.tokopedia.kol.feature.report.domain.usecase.SendReportUseCase
import com.tokopedia.kol.feature.report.view.listener.ContentReportContract
import com.tokopedia.kotlin.extensions.view.debugTrace
import rx.Subscriber

/**
 * @author by milhamj on 15/11/18.
 */
class ContentReportSubscriber(val view: ContentReportContract.View?)
    : Subscriber<SendReportResponse>() {
    override fun onNext(response: SendReportResponse) {
        if (response.feedReportSubmit.errorMessage.isEmpty().not()) {
            if (response.feedReportSubmit.errorType == SendReportUseCase.ERROR_REPORT_DUPLICATE) {
                view?.onErrorSendReportDuplicate(response.feedReportSubmit.errorMessage)
            } else {
                onError(MessageErrorException(response.feedReportSubmit.errorMessage))
            }
            return
        }

        if (response.feedReportSubmit.data.success == SendReportUseCase.REPORT_SUCCESS) {
            view?.onSuccessSendReport()
        } else {
            onError(MessageErrorException(view?.getContext()!!.getString(R.string.kol_report_error)))
        }
    }

    override fun onCompleted() {
    }

    override fun onError(e: Throwable?) {
        e?.debugTrace()

        view?.hideLoading()
        view?.onErrorSendReport(ErrorHandler.getErrorMessage(view.getContext(), e))
    }
}