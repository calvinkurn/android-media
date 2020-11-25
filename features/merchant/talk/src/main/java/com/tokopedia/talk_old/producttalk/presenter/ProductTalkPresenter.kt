package com.tokopedia.talk_old.producttalk.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.talk_old.common.di.TalkScope
import com.tokopedia.talk_old.common.domain.usecase.DeleteCommentTalkUseCase
import com.tokopedia.talk_old.common.domain.usecase.DeleteTalkUseCase
import com.tokopedia.talk_old.common.domain.usecase.FollowUnfollowTalkUseCase
import com.tokopedia.talk_old.common.domain.usecase.MarkTalkNotFraudUseCase
import com.tokopedia.talk_old.common.view.BaseActionTalkViewModel
import com.tokopedia.talk_old.producttalk.domain.usecase.GetProductTalkUseCase
import com.tokopedia.talk_old.producttalk.view.listener.ProductTalkContract
import com.tokopedia.talk_old.producttalk.view.viewmodel.ProductTalkViewModel
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber
import javax.inject.Inject

/**
 * @author by Steven
 */
class ProductTalkPresenter @Inject constructor(@TalkScope val userSession: UserSessionInterface,
                                               private val deleteTalkUseCase: DeleteTalkUseCase,
                                               @TalkScope val getProductTalkUseCase: GetProductTalkUseCase,
                                               @TalkScope val deleteCommentTalkUseCase: DeleteCommentTalkUseCase,
                                               private val followUnfollowTalkUseCase: FollowUnfollowTalkUseCase,
                                               private val markTalkNotFraudUseCase: MarkTalkNotFraudUseCase) :
        ProductTalkContract.Presenter,
        BaseDaggerPresenter<ProductTalkContract.View>() {

    override fun attachView(view: ProductTalkContract.View?) {
        super.attachView(view)

    }

    var isRequesting: Boolean = false
    var page: Int = 1
    var pageId: Int = 0

    override fun initProductTalk(productId: String) {
        view.showLoadingFull()
        page = 1
        getProductTalk(productId, true)
    }

    override fun getProductTalk(productId: String) {
        getProductTalk(productId, false)
    }

    override fun resetProductTalk(productId: String) {
        view.showRefresh()
        page = 1
        getProductTalk(productId, true)
    }

    fun getProductTalk(productId: String, reset: Boolean) {
        if (!isRequesting) {
            isRequesting = true
            getProductTalkUseCase.execute(GetProductTalkUseCase.getParam(userSession.userId, page, productId)
                    , object : Subscriber<ProductTalkViewModel>() {
                override fun onNext(viewModel: ProductTalkViewModel) {
                    isRequesting = false
                    view.hideLoadingFull()
                    if (viewModel.listThread.isEmpty()) {
                        view.hideRefresh()
                        view.onEmptyTalk(viewModel)
                    } else {
                        if (reset) {
                            view.hideRefresh()
                            view.onSuccessResetTalk(viewModel)
                        } else {
                            view.onSuccessGetTalks(viewModel)
                        }
                        if (viewModel.hasNextPage) {
                            view.setCanLoad()
                            page += 1
                            pageId = viewModel.page_id
                        }
                    }
                }

                override fun onCompleted() {

                }

                override fun onError(e: Throwable) {
                    isRequesting = false
                    view.hideRefresh()
                    view.hideLoadingFull()
                    onErrorTalk(e)
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
                    onErrorTalk(e)
                }

                override fun onNext(talkViewModel: BaseActionTalkViewModel) {
                    view.hideLoadingAction()
                    view.onSuccessDeleteTalk(talkId)
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
                    onErrorTalk(e)
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
                    onErrorTalk(e)
                }

                override fun onNext(talkViewModel: BaseActionTalkViewModel) {
                    view.hideLoadingAction()
                    view.onSuccessFollowTalk(talkId)
                }
            })
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
                    onErrorTalk(e)
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
                    onErrorTalk(e)
                }

                override fun onNext(talkViewModel: BaseActionTalkViewModel) {
                    view.hideLoadingAction()
                    view.onSuccessMarkCommentNotFraud(talkId, commentId)
                }
            })
        }
    }

    private fun onErrorTalk(e: Throwable) {
        if (e is MessageErrorException) {
            view.onErrorGetTalks(e.message ?: "")
        } else {
            view.onErrorGetTalks(ErrorHandler.getErrorMessage(view.getContext(), e))
        }
    }

    override fun deleteCommentTalk(shopId: String, talkId: String, commentId: String) {
        if (!isRequesting) {
            isRequesting = true
            view.showLoadingAction()
            deleteCommentTalkUseCase.execute(DeleteCommentTalkUseCase.getParam(
                    shopId,
                    talkId,
                    commentId
            ), object : Subscriber<BaseActionTalkViewModel>() {
                override fun onCompleted() {

                }

                override fun onError(e: Throwable) {
                    isRequesting = false
                    view.hideLoadingAction()
                    onErrorTalk(e)
                }

                override fun onNext(talkViewModel: BaseActionTalkViewModel) {
                    isRequesting = false
                    view.hideLoadingAction()
                    view.onSuccessDeleteCommentTalk(talkId, commentId)

                }
            })
        }
    }

    override fun isLoggedIn(): Boolean {
        return userSession.isLoggedIn
    }

    override fun isMyShop(shopId: String): Boolean {
        return userSession.shopId == shopId
    }


    override fun detachView() {
        getProductTalkUseCase.unsubscribe()
        deleteTalkUseCase.unsubscribe()
        deleteCommentTalkUseCase.unsubscribe()
        markTalkNotFraudUseCase.unsubscribe()
        followUnfollowTalkUseCase.unsubscribe()
        super.detachView()
    }
}