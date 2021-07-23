package com.tokopedia.kol.feature.report.view.subscriber

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.feedcomponent.domain.model.report.entity.SendReportResponse
import com.tokopedia.kol.R
import com.tokopedia.kol.feature.report.domain.usecase.SendReportUseCase
import com.tokopedia.kol.feature.report.view.listener.ContentReportContract
import rx.Subscriber
import timber.log.Timber

/**
 * @author by milhamj on 15/11/18.
 */
class ContentReportSubscriber(val view: ContentReportContract.View?)
    : Subscriber<SendReportResponse>() {
    override fun onNext(response: SendReportResponse) {
        if (response.feedReportSubmit.errorMessage.isNotEmpty()) {
            if (response.feedReportSubmit.errorType == SendReportUseCase.ERROR_REPORT_DUPLICATE) {
                view?.run {
                    onErrorSendReportDuplicate(response.feedReportSubmit.errorMessage)
                }
            } else {
                onError(MessageErrorException(response.feedReportSubmit.errorMessage))
            }
            return
        }

        if (response.feedReportSubmit.data.success == SendReportUseCase.REPORT_SUCCESS) {
            view?.run {
                onSuccessSendReport()
            }
        } else {
            view?.run {
                onError(MessageErrorException(getContext()?.getString(R.string.kol_report_error)))
            }
        }
    }

    override fun onCompleted() {
    }

    override fun onError(e: Throwable?) {
        Timber.d(e)
        view?.run {
            hideLoading()
            onErrorSendReport(ErrorHandler.getErrorMessage(view.getContext(), e))
        }
    }
}