package com.tokopedia.talk.reporttalk.view

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.talk.common.view.BaseActionTalkViewModel
import com.tokopedia.talk.reporttalk.domain.ReportTalkUseCase
import com.tokopedia.talk.reporttalk.view.listener.ReportTalkContract
import rx.Subscriber
import javax.inject.Inject

/**
 * @author by nisie on 8/30/18.
 */
class ReportTalkPresenter @Inject constructor(val reportTalkUseCase: ReportTalkUseCase)
    : BaseDaggerPresenter<ReportTalkContract.View>(),
        ReportTalkContract.Presenter {

    override fun reportTalk(talkId: String, shopId: String, productId: String, reason: String) {

            view.showLoadingFull()
            reportTalkUseCase.execute(ReportTalkUseCase.getParam(
                    productId,
                    shopId,
                    talkId,
                    reason
            ), object : Subscriber<BaseActionTalkViewModel>() {
                override fun onCompleted() {

                }

                override fun onError(e: Throwable) {
                    view.hideLoadingFull()
                    if (e is MessageErrorException) {
                        view.onErrorReportTalk(e.message ?: "")
                    } else {
                        view.onErrorReportTalk(ErrorHandler.getErrorMessage(view.getContext(), e))
                    }
                }

                override fun onNext(talkViewModel: BaseActionTalkViewModel) {
                    view.hideLoadingFull()
                    view.onSuccessReportTalk()
                }
            })

    }

    override fun detachView() {
        super.detachView()
        reportTalkUseCase.unsubscribe()
    }

}