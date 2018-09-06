package com.tokopedia.talk.inboxtalk.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.talk.common.di.TalkScope
import com.tokopedia.talk.inboxtalk.domain.GetInboxTalkUseCase
import com.tokopedia.talk.inboxtalk.view.listener.InboxTalkContract
import com.tokopedia.talk.inboxtalk.view.viewmodel.InboxTalkViewModel
import rx.Subscriber
import javax.inject.Inject

/**
 * @author by nisie on 8/29/18.
 */
class InboxTalkPresenter @Inject constructor(@TalkScope val getInboxTalkUseCase:
                                             GetInboxTalkUseCase)
    : BaseDaggerPresenter<InboxTalkContract.View>(),
        InboxTalkContract.Presenter {

    var page: Int = 1
    var page_id: Int = 0

    override fun getInboxTalk(filter: String, nav: String) {
        view.showLoadingFull()

        getInboxTalkUseCase.execute(GetInboxTalkUseCase.getParam(
                filter,
                nav,
                page,
                page_id
        ), object : Subscriber<InboxTalkViewModel>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                view.hideLoadingFull()
                if (e is MessageErrorException) {
                    view.onErrorGetInboxTalk(e.message ?: "")
                } else {
                    view.onErrorGetInboxTalk(ErrorHandler.getErrorMessage(view.getContext(), e))
                }
            }

            override fun onNext(talkViewModel: InboxTalkViewModel) {
                view.hideLoadingFull()
                if (talkViewModel.listTalk.isEmpty()) {
                    view.onEmptyTalk()
                } else {
                    view.onSuccessGetInboxTalk(talkViewModel.listTalk)
                    if (talkViewModel.hasNextPage) {
                        page += 1
                        page_id = talkViewModel.page_id
                    }
                }
            }
        })
    }

    override fun refreshTalk(filter: String, nav: String) {
        page = 1
        page_id = 0

        getInboxTalkUseCase.execute(GetInboxTalkUseCase.getParam(
                filter,
                nav,
                page,
                page_id
        ), object : Subscriber<InboxTalkViewModel>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                view.hideRefreshLoad()
                if (e is MessageErrorException) {
                    view.onErrorGetInboxTalk(e.message ?: "")
                } else if (GlobalConfig.isAllowDebuggingTools()) {
                    view.onErrorGetInboxTalk(e.toString())
                } else {
                    view.onErrorGetInboxTalk(ErrorHandler.getErrorMessage(view.getContext(), e))
                }
            }

            override fun onNext(talkViewModel: InboxTalkViewModel) {
                view.hideRefreshLoad()
                if (talkViewModel.listTalk.isEmpty()) {
                    view.onEmptyTalk()
                } else {
                    view.onSuccessRefreshInboxTalk(talkViewModel.listTalk)
                    if (talkViewModel.hasNextPage) {
                        page += 1
                        page_id = talkViewModel.page_id
                    }
                }
            }
        })
    }

    override fun detachView() {
        super.detachView()
        getInboxTalkUseCase.unsubscribe()
    }

}