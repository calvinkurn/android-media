package com.tokopedia.kol.feature.comment.view.subscriber

import com.tokopedia.feedcomponent.domain.model.report.entity.SendReportResponse
import com.tokopedia.kol.R
import com.tokopedia.kol.feature.comment.view.listener.KolComment
import com.tokopedia.kol.feature.report.domain.usecase.SendReportUseCase
import rx.Subscriber
import timber.log.Timber

class ReportKolSubscriber(val view: KolComment.View) : Subscriber<SendReportResponse>() {
    override fun onNext(response: SendReportResponse) {
        if (response.feedReportSubmit.errorMessage.isNotEmpty()) {
            if (response.feedReportSubmit.errorType == SendReportUseCase.ERROR_REPORT_DUPLICATE) {
                view.run {
                    onErrorSendReport(response.feedReportSubmit.errorMessage)
                }
            } else {
                onError(Exception(response.feedReportSubmit.errorMessage))
            }
            return
        }

        if (response.feedReportSubmit.data.success == SendReportUseCase.REPORT_SUCCESS) {
            view.run {
                onSuccessSendReport()
            }
        } else {
            view.run {
                onError(Exception(context?.getString(R.string.kol_report_error)))
            }
        }
    }

    override fun onCompleted() {
    }

    override fun onError(e: Throwable?) {
        Timber.d(e)
        view.run {
            onErrorSendReport(e?.message)
        }
    }
}