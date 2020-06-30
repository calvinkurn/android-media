package com.tokopedia.talk_old.inboxtalk.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.talk_old.common.domain.usecase.DeleteCommentTalkUseCase
import com.tokopedia.talk_old.common.domain.usecase.DeleteTalkUseCase
import com.tokopedia.talk_old.common.domain.usecase.FollowUnfollowTalkUseCase
import com.tokopedia.talk_old.common.domain.usecase.MarkTalkNotFraudUseCase
import com.tokopedia.talk_old.common.view.BaseActionTalkViewModel
import com.tokopedia.talk_old.inboxtalk.domain.GetInboxTalkUseCase
import com.tokopedia.talk_old.inboxtalk.view.listener.InboxTalkContract
import com.tokopedia.talk_old.inboxtalk.view.viewmodel.InboxTalkViewModel
import rx.Subscriber
import javax.inject.Inject

/**
 * @author by nisie on 8/29/18.
 */
class InboxTalkPresenter @Inject constructor(private val getInboxTalkUseCase: GetInboxTalkUseCase,
                                             private val deleteTalkUseCase: DeleteTalkUseCase,
                                             private val deleteCommentTalkUseCase:
                                             DeleteCommentTalkUseCase,
                                             private val followUnfollowTalkUseCase:
                                             FollowUnfollowTalkUseCase,
                                             private val markTalkNotFraudUseCase: MarkTalkNotFraudUseCase)
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
                    isRequesting = false

                    if (page == 1 && talkViewModel.listTalk.isEmpty()) {
                        view.hideFilter()
                        view.onEmptyTalk()
                    } else if (!talkViewModel.listTalk.isEmpty()) {
                        view.onSuccessGetInboxTalk(talkViewModel)
                        view.showFilter()
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
            view.showLoadingAction()
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
                    view.hideLoadingAction()
                    onErrorTalk(e)

                }

                override fun onNext(talkViewModel: InboxTalkViewModel) {
                    view.hideLoadingAction()
                    view.showFilter()
                    isRequesting = false

                    if (page == 1 && talkViewModel.listTalk.isEmpty()) {
                        view.onEmptyTalk()
                    } else if (!talkViewModel.listTalk.isEmpty()) {
                        view.onSuccessGetListFirstPage(talkViewModel)
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
                        view.onSuccessGetListFirstPage(talkViewModel)
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

    private fun onErrorActionTalk(e: Throwable) {
        view.showFilter()
        isRequesting = false

        if (e is MessageErrorException) {
            view.onErrorActionTalk(e.message ?: "")
        } else {
            view.onErrorActionTalk(ErrorHandler.getErrorMessage(view.getContext(), e))
        }
    }

    override fun deleteTalk(shopId: String, talkId: String) {
        if (!isRequesting) {
            view.showLoadingAction()
            view.hideFilter()

            deleteTalkUseCase.execute(DeleteTalkUseCase.getParam(
                    shopId,
                    talkId
            ), object : Subscriber<BaseActionTalkViewModel>() {
                override fun onCompleted() {

                }

                override fun onError(e: Throwable) {
                    view.hideLoadingAction()
                    view.showFilter()
                    onErrorActionTalk(e)
                }

                override fun onNext(talkViewModel: BaseActionTalkViewModel) {
                    view.hideLoadingAction()
                    view.showFilter()
                    view.onSuccessDeleteTalk(talkId)
                }
            })
        }
    }

    override fun deleteCommentTalk(shopId: String, talkId: String, commentId: String) {
        if (!isRequesting) {
            view.showLoadingAction()
            view.hideFilter()
            deleteCommentTalkUseCase.execute(DeleteCommentTalkUseCase.getParam(
                    shopId,
                    talkId,
                    commentId
            ), object : Subscriber<BaseActionTalkViewModel>() {
                override fun onCompleted() {

                }

                override fun onError(e: Throwable) {
                    view.hideLoadingAction()
                    view.showFilter()
                    onErrorActionTalk(e)
                }

                override fun onNext(talkViewModel: BaseActionTalkViewModel) {
                    view.hideLoadingAction()
                    view.showFilter()
                    view.onSuccessDeleteCommentTalk(talkId, commentId)

                }
            })
        }
    }

    override fun unfollowTalk(talkId: String) {
        if (!isRequesting) {
            view.showLoadingAction()
            view.hideFilter()
            followUnfollowTalkUseCase.execute(FollowUnfollowTalkUseCase.getParam(
                    talkId
            ), object : Subscriber<BaseActionTalkViewModel>() {
                override fun onCompleted() {

                }

                override fun onError(e: Throwable) {
                    view.hideLoadingAction()
                    view.showFilter()
                    onErrorActionTalk(e)
                }

                override fun onNext(talkViewModel: BaseActionTalkViewModel) {
                    view.hideLoadingAction()
                    view.showFilter()
                    view.onSuccessUnfollowTalk(talkId)

                }
            })
        }
    }

    override fun followTalk(talkId: String) {
        if (!isRequesting) {
            view.showLoadingAction()
            view.hideFilter()
            followUnfollowTalkUseCase.execute(FollowUnfollowTalkUseCase.getParam(
                    talkId
            ), object : Subscriber<BaseActionTalkViewModel>() {
                override fun onCompleted() {

                }

                override fun onError(e: Throwable) {
                    view.hideLoadingAction()
                    view.showFilter()
                    onErrorActionTalk(e)
                }

                override fun onNext(talkViewModel: BaseActionTalkViewModel) {
                    view.hideLoadingAction()
                    view.showFilter()
                    view.onSuccessFollowTalk(talkId)
                }
            })
        }
    }

    override fun markTalkNotFraud(talkId: String) {
        if (!isRequesting) {
            view.showLoadingAction()
            view.hideFilter()
            markTalkNotFraudUseCase.execute(MarkTalkNotFraudUseCase.getParamTalk(
                    talkId
            ), object : Subscriber<BaseActionTalkViewModel>() {
                override fun onCompleted() {

                }

                override fun onError(e: Throwable) {
                    view.hideLoadingAction()
                    view.showFilter()
                    onErrorActionTalk(e)
                }

                override fun onNext(talkViewModel: BaseActionTalkViewModel) {
                    view.hideLoadingAction()
                    view.showFilter()
                    view.onSuccessMarkTalkNotFraud(talkId)
                }
            })
        }
    }

    override fun markCommentNotFraud(talkId: String, commentId: String) {
        if (!isRequesting) {
            view.showLoadingAction()
            view.hideFilter()
            markTalkNotFraudUseCase.execute(MarkTalkNotFraudUseCase.getParamTalk(
                    talkId
            ), object : Subscriber<BaseActionTalkViewModel>() {
                override fun onCompleted() {

                }

                override fun onError(e: Throwable) {
                    view.hideLoadingAction()
                    view.showFilter()
                    onErrorActionTalk(e)
                }

                override fun onNext(talkViewModel: BaseActionTalkViewModel) {
                    view.hideLoadingAction()
                    view.showFilter()
                    view.onSuccessMarkCommentNotFraud(talkId, commentId)
                }
            })
        }
    }

    override fun detachView() {
        super.detachView()
        getInboxTalkUseCase.unsubscribe()
        deleteTalkUseCase.unsubscribe()
        deleteCommentTalkUseCase.unsubscribe()
        followUnfollowTalkUseCase.unsubscribe()
        markTalkNotFraudUseCase.unsubscribe()
    }

}