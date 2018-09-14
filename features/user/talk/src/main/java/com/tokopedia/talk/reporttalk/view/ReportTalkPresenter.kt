package com.tokopedia.talk.reporttalk.view

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.talk.common.view.BaseActionTalkViewModel
import com.tokopedia.talk.reporttalk.domain.ReportTalkUseCase
import com.tokopedia.talk.reporttalk.view.listener.ReportTalkContract
import com.tokopedia.talk.reporttalk.view.viewmodel.TalkReportOptionViewModel
import rx.Subscriber
import javax.inject.Inject

/**
 * @author by nisie on 8/30/18.
 */
class ReportTalkPresenter @Inject constructor(private val reportTalkUseCase: ReportTalkUseCase)
    : BaseDaggerPresenter<ReportTalkContract.View>(),
        ReportTalkContract.Presenter {

    override fun reportTalk(talkId: String, shopId: String, productId: String, otherReason: String,
                            selectedOption: TalkReportOptionViewModel) {
        var reason = selectedOption.reportTitle
        if (selectedOption.isChecked) {
            if (selectedOption.position == 2) reason = otherReason
            view.showLoadingFull()
            reportTalkUseCase.execute(ReportTalkUseCase.getParamTalk(
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

    }

    override fun detachView() {
        super.detachView()
        reportTalkUseCase.unsubscribe()
    }

}