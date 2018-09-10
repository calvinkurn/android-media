package com.tokopedia.talk.inboxtalk.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.talk.common.domain.usecase.DeleteCommentTalkUseCase
import com.tokopedia.talk.common.domain.usecase.DeleteTalkUseCase
import com.tokopedia.talk.common.view.BaseActionTalkViewModel
import com.tokopedia.talk.inboxtalk.domain.GetInboxTalkUseCase
import com.tokopedia.talk.inboxtalk.view.listener.InboxTalkContract
import com.tokopedia.talk.inboxtalk.view.viewmodel.InboxTalkViewModel
import rx.Subscriber
import javax.inject.Inject

/**
 * @author by nisie on 8/29/18.
 */
class InboxTalkPresenter @Inject constructor(private val getInboxTalkUseCase: GetInboxTalkUseCase,
                                             private val deleteTalkUseCase: DeleteTalkUseCase,
                                             private val deleteCommentTalkUseCase: DeleteCommentTalkUseCase)
    : BaseDaggerPresenter<InboxTalkContract.View>(),
        InboxTalkContract.Presenter {

    var isRequesting: Boolean = false
    var page: Int = 1
    var page_id: Int = 0

    override fun getInboxTalk(filter: String, nav: String) {
        if (!isRequesting) {
            view.showLoading()
            view.hideFilter()
            isRequesting = true
            getInboxTalkUseCase.execute(GetInboxTalkUseCase.getParam(
                    filter,
                    nav,
                    page,
                    page_id
            ), object : Subscriber<InboxTalkViewModel>() {
                override fun onCompleted() {

                }

                override fun onError(e: Throwable) {
                    view.hideLoading()
                    onErrorTalk(e)
                }

                override fun onNext(talkViewModel: InboxTalkViewModel) {
                    view.hideLoading()
                    view.showFilter()
                    isRequesting = false

                    if (page == 1 && talkViewModel.listTalk.isEmpty()) {
                        view.onEmptyTalk()
                    } else if (!talkViewModel.listTalk.isEmpty()) {
                        view.onSuccessGetInboxTalk(talkViewModel)
                    }

                    if (talkViewModel.hasNextPage) {
                        page += 1
                        page_id = talkViewModel.page_id
                        view.showLoading()
                    } else {
                        view.hideLoading()
                    }

                }
            })
        }
    }

    override fun getInboxTalkWithFilter(filter: String, nav: String) {
        if (!isRequesting) {
            page = 1
            page_id = 0
            view.showLoadingFilter()
            view.hideFilter()
            isRequesting = true
            getInboxTalkUseCase.execute(GetInboxTalkUseCase.getParam(
                    filter,
                    nav,
                    page,
                    page_id
            ), object : Subscriber<InboxTalkViewModel>() {
                override fun onCompleted() {

                }

                override fun onError(e: Throwable) {
                    view.hideLoadingFilter()
                    onErrorTalk(e)

                }

                override fun onNext(talkViewModel: InboxTalkViewModel) {
                    view.hideLoadingFilter()
                    view.showFilter()
                    isRequesting = false

                    if (page == 1 && talkViewModel.listTalk.isEmpty()) {
                        view.onEmptyTalk()
                    } else if (!talkViewModel.listTalk.isEmpty()) {
                        view.onSuccessGetListFirstPage(talkViewModel.listTalk)
                    }

                    if (talkViewModel.hasNextPage) {
                        page += 1
                        page_id = talkViewModel.page_id
                        view.showLoading()
                    } else {
                        view.hideLoading()
                    }

                }
            })
        }
    }


    override fun refreshTalk(filter: String, nav: String) {
        if (!isRequesting) {
            page = 1
            page_id = 0

            view.hideFilter()
            isRequesting = true

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
                    onErrorTalk(e)
                }

                override fun onNext(talkViewModel: InboxTalkViewModel) {
                    view.hideRefreshLoad()
                    view.showFilter()
                    isRequesting = false

                    if (talkViewModel.listTalk.isEmpty()) {
                        view.onEmptyTalk()
                    } else {
                        view.onSuccessGetListFirstPage(talkViewModel.listTalk)
                        if (talkViewModel.hasNextPage) {
                            page += 1
                            page_id = talkViewModel.page_id
                        }
                    }

                    if (talkViewModel.hasNextPage) {
                        page += 1
                        page_id = talkViewModel.page_id
                        view.showLoading()
                    } else {
                        view.hideLoading()
                    }

                }
            })
        } else {
            view.hideRefreshLoad()
        }
    }

    private fun onErrorTalk(e: Throwable) {
        view.showFilter()
        isRequesting = false

        if (e is MessageErrorException) {
            view.onErrorGetInboxTalk(e.message ?: "")
        } else {
            view.onErrorGetInboxTalk(ErrorHandler.getErrorMessage(view.getContext(), e))
        }
    }

    override fun deleteTalk() {
        if (!isRequesting) {

            deleteTalkUseCase.execute(DeleteTalkUseCase.getParam(
                    "",
                    ""
            ), object : Subscriber<BaseActionTalkViewModel>() {
                override fun onCompleted() {

                }

                override fun onError(e: Throwable) {
                    view.hideRefreshLoad()
                    onErrorTalk(e)
                }

                override fun onNext(talkViewModel: BaseActionTalkViewModel) {
                    if (talkViewModel.isSuccess) {
                        view.onSuccessDeleteTalk()
                    }
                }
            })
        }
    }

    override fun deleteCommentTalk() {
        if (!isRequesting) {

            deleteCommentTalkUseCase.execute(DeleteCommentTalkUseCase.getParam(
                    "",
                    "",
                    ""
            ), object : Subscriber<BaseActionTalkViewModel>() {
                override fun onCompleted() {

                }

                override fun onError(e: Throwable) {
                    view.hideRefreshLoad()
                    onErrorTalk(e)
                }

                override fun onNext(talkViewModel: BaseActionTalkViewModel) {
                    if (talkViewModel.isSuccess) {
                        view.onSuccessDeleteCommentTalk()
                    }
                }
            })
        }
    }

    override fun unfollowTalk() {
        if (!isRequesting) {

            deleteCommentTalkUseCase.execute(DeleteCommentTalkUseCase.getParam(
                    "",
                    "",
                    ""
            ), object : Subscriber<BaseActionTalkViewModel>() {
                override fun onCompleted() {

                }

                override fun onError(e: Throwable) {
                    view.hideRefreshLoad()
                    onErrorTalk(e)
                }

                override fun onNext(talkViewModel: BaseActionTalkViewModel) {
                    if (talkViewModel.isSuccess) {
                        view.onSuccessDeleteCommentTalk()
                    }
                }
            })
        }
    }

    override fun followTalk() {

    }

    override fun detachView() {
        super.detachView()
        getInboxTalkUseCase.unsubscribe()
        deleteTalkUseCase.unsubscribe()
        deleteCommentTalkUseCase.unsubscribe()
    }

}