package com.tokopedia.kol.feature.report.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.kol.feature.report.domain.usecase.SendReportUseCase
import com.tokopedia.kol.feature.report.view.listener.ContentReportContract
import com.tokopedia.kol.feature.report.view.subscriber.ContentReportSubscriber
import javax.inject.Inject

/**
 * @author by milhamj on 12/11/18.
 */
class ContentReportPresenter @Inject constructor(private val sendReportUseCase: SendReportUseCase)
    : BaseDaggerPresenter<ContentReportContract.View>(), ContentReportContract.Presenter {

    override fun detachView() {
        super.detachView()
        sendReportUseCase.unsubscribe()
    }

    override fun sendReport(contentId: Int, reasonType: String, reasonMessage: String, contentType: String) {
        view.showLoading()
        sendReportUseCase.execute(
                SendReportUseCase.createRequestParams(contentId, reasonType, reasonMessage, contentType),
                ContentReportSubscriber(view)
        )
    }
}