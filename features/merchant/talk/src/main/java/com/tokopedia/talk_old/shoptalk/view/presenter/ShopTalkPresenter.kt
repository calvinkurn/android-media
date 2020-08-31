package com.tokopedia.talk_old.shoptalk.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.talk_old.common.domain.usecase.DeleteCommentTalkUseCase
import com.tokopedia.talk_old.common.domain.usecase.DeleteTalkUseCase
import com.tokopedia.talk_old.common.domain.usecase.FollowUnfollowTalkUseCase
import com.tokopedia.talk_old.common.domain.usecase.MarkTalkNotFraudUseCase
import com.tokopedia.talk_old.common.view.BaseActionTalkViewModel
import com.tokopedia.talk_old.inboxtalk.view.viewmodel.InboxTalkViewModel
import com.tokopedia.talk_old.shoptalk.domain.GetShopTalkUseCase
import com.tokopedia.talk_old.shoptalk.view.listener.ShopTalkContract
import rx.Subscriber
import javax.inject.Inject

/**
 * @author by nisie on 9/17/18.
 */
class ShopTalkPresenter @Inject constructor(private val getShopTalkUseCase: GetShopTalkUseCase,
                                            private val deleteTalkUseCase: DeleteTalkUseCase,
                                            private val deleteCommentTalkUseCase:
                                            DeleteCommentTalkUseCase,
                                            private val followUnfollowTalkUseCase:
                                            FollowUnfollowTalkUseCase,
                                            private val markTalkNotFraudUseCase: MarkTalkNotFraudUseCase)
    : BaseDaggerPresenter<ShopTalkContract.View>(),
        ShopTalkContract.Presenter {

    var isRequesting: Boolean = false
    var page: Int = 1
    var page_id: Int = 0

    override fun getShopTalk(shopId: String) {
        if (!isRequesting) {
            view.showLoading()
            isRequesting = true
            getShopTalkUseCase.execute(GetShopTalkUseCase.getParam(
                    shopId,
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
                        view.onEmptyTalk()
                    } else if (!talkViewModel.listTalk.isEmpty()) {
                        view.onSuccessGetShopTalk(talkViewModel)
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

    override fun refreshTalk(shopId: String) {

        if (!isRequesting) {
            page = 1
            page_id = 0

            isRequesting = true

            getShopTalkUseCase.execute(GetShopTalkUseCase.getParam(
                    shopId,
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
                    isRequesting = false

                    if (talkViewModel.listTalk.isEmpty()) {
                        view.onEmptyTalk()
                    } else {
                        view.onSuccessRefreshTalk(talkViewModel.listTalk)
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

    override fun markTalkNotFraud(talkId: String) {
        if (!isRequesting) {
            view.showLoadingAction()
            markTalkNotFraudUseCase.execute(MarkTalkNotFraudUseCase.getParamTalk(
                    talkId
            ), object : Subscriber<BaseActionTalkViewModel>() {
                override fun onCompleted() {

                }

                override fun onError(e: Throwable) {
                    view.hideLoadingAction()
                    onErrorActionTalk(e)
                }

                override fun onNext(talkViewModel: BaseActionTalkViewModel) {
                    view.hideLoadingAction()
                    view.onSuccessMarkTalkNotFraud(talkId)
                }
            })
        }
    }

    override fun markCommentNotFraud(talkId: String, commentId: String) {
        if (!isRequesting) {
            view.showLoadingAction()
            markTalkNotFraudUseCase.execute(MarkTalkNotFraudUseCase.getParamTalk(
                    talkId
            ), object : Subscriber<BaseActionTalkViewModel>() {
                override fun onCompleted() {

                }

                override fun onError(e: Throwable) {
                    view.hideLoadingAction()
                    onErrorActionTalk(e)
                }

                override fun onNext(talkViewModel: BaseActionTalkViewModel) {
                    view.hideLoadingAction()
                    view.onSuccessMarkCommentNotFraud(talkId, commentId)
                }
            })
        }
    }

    override fun unfollowTalk(talkId: String) {
        if (!isRequesting) {
            view.showLoadingAction()
            followUnfollowTalkUseCase.execute(FollowUnfollowTalkUseCase.getParam(
                    talkId
            ), object : Subscriber<BaseActionTalkViewModel>() {
                override fun onCompleted() {

                }

                override fun onError(e: Throwable) {
                    view.hideLoadingAction()
                    onErrorActionTalk(e)
                }

                override fun onNext(talkViewModel: BaseActionTalkViewModel) {
                    view.hideLoadingAction()
                    view.onSuccessUnfollowTalk(talkId)

                }
            })
        }
    }

    override fun followTalk(talkId: String) {
        if (!isRequesting) {
            view.showLoadingAction()
            followUnfollowTalkUseCase.execute(FollowUnfollowTalkUseCase.getParam(
                    talkId
            ), object : Subscriber<BaseActionTalkViewModel>() {
                override fun onCompleted() {

                }

                override fun onError(e: Throwable) {
                    view.hideLoadingAction()
                    onErrorActionTalk(e)
                }

                override fun onNext(talkViewModel: BaseActionTalkViewModel) {
                    view.hideLoadingAction()
                    view.onSuccessFollowTalk(talkId)
                }
            })
        }
    }

    override fun deleteTalk(shopId: String, talkId: String) {
        if (!isRequesting) {
            view.showLoadingAction()

            deleteTalkUseCase.execute(DeleteTalkUseCase.getParam(
                    shopId,
                    talkId
            ), object : Subscriber<BaseActionTalkViewModel>() {
                override fun onCompleted() {

                }

                override fun onError(e: Throwable) {
                    view.hideLoadingAction()
                    onErrorActionTalk(e)
                }

                override fun onNext(talkViewModel: BaseActionTalkViewModel) {
                    view.hideLoadingAction()
                    view.onSuccessDeleteTalk(talkId)
                }
            })
        }
    }

    override fun deleteCommentTalk(shopId: String, talkId: String, commentId: String) {
        if (!isRequesting) {
            view.showLoadingAction()
            deleteCommentTalkUseCase.execute(DeleteCommentTalkUseCase.getParam(
                    shopId,
                    talkId,
                    commentId
            ), object : Subscriber<BaseActionTalkViewModel>() {
                override fun onCompleted() {

                }

                override fun onError(e: Throwable) {
                    view.hideLoadingAction()
                    onErrorActionTalk(e)
                }

                override fun onNext(talkViewModel: BaseActionTalkViewModel) {
                    view.hideLoadingAction()
                    view.onSuccessDeleteCommentTalk(talkId, commentId)

                }
            })
        }
    }

    private fun onErrorTalk(e: Throwable) {
        isRequesting = false

        if (e is MessageErrorException) {
            view.onErrorGetShopTalk(e.message ?: "")
        } else {
            view.onErrorGetShopTalk(ErrorHandler.getErrorMessage(view.getContext(), e))
        }
    }

    private fun onErrorActionTalk(e: Throwable) {
        isRequesting = false

        if (e is MessageErrorException) {
            view.onErrorActionTalk(e.message ?: "")
        } else {
            view.onErrorActionTalk(ErrorHandler.getErrorMessage(view.getContext(), e))
        }
    }

    override fun detachView() {
        super.detachView()
        getShopTalkUseCase.unsubscribe()
        deleteTalkUseCase.unsubscribe()
        deleteCommentTalkUseCase.unsubscribe()
        followUnfollowTalkUseCase.unsubscribe()
        markTalkNotFraudUseCase.unsubscribe()
    }

}