package com.tokopedia.talk_old.talkdetails.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.talk_old.common.adapter.viewmodel.TalkProductAttachmentViewModel
import com.tokopedia.talk_old.common.domain.usecase.DeleteCommentTalkUseCase
import com.tokopedia.talk_old.common.domain.usecase.DeleteTalkUseCase
import com.tokopedia.talk_old.common.domain.usecase.FollowUnfollowTalkUseCase
import com.tokopedia.talk_old.common.domain.usecase.MarkTalkNotFraudUseCase
import com.tokopedia.talk_old.common.view.BaseActionTalkViewModel
import com.tokopedia.talk_old.talkdetails.data.SendCommentResponse
import com.tokopedia.talk_old.talkdetails.domain.usecase.GetTalkCommentsUseCase
import com.tokopedia.talk_old.talkdetails.domain.usecase.SendCommentsUseCase
import com.tokopedia.talk_old.talkdetails.view.contract.TalkDetailsContract
import com.tokopedia.talk_old.talkdetails.view.viewmodel.TalkDetailViewModel
import rx.Subscriber
import javax.inject.Inject

/**
 * Created by Hendri on 28/08/18.
 */
class TalkDetailsPresenter @Inject constructor(private val getTalkComments: GetTalkCommentsUseCase,
                                               private val sendCommentsUseCase: SendCommentsUseCase,
                                               private val deleteTalkUseCase: DeleteTalkUseCase,
                                               private val deleteCommentTalkUseCase: DeleteCommentTalkUseCase,
                                               private val followUnfollowTalkUseCase: FollowUnfollowTalkUseCase,
                                               private val markTalkNotFraudUseCase: MarkTalkNotFraudUseCase) :
        BaseDaggerPresenter<TalkDetailsContract.View>(),
        TalkDetailsContract.Presenter {

    override fun loadTalkDetails(id: String) {
        getTalkComments.execute(GetTalkCommentsUseCase.getParameters(id),
                object : Subscriber<TalkDetailViewModel>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        view.onError(e)
                    }

                    override fun onNext(response: TalkDetailViewModel) {
                        view.onSuccessLoadTalkDetails(response.listTalk)
                    }
                })
    }

    override fun refreshTalkAfterSendComment(talkId: String) {
        getTalkComments.execute(GetTalkCommentsUseCase.getParameters(talkId),
                object : Subscriber<TalkDetailViewModel>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        view.onError(e)
                    }

                    override fun onNext(response: TalkDetailViewModel) {
                        view.onSuccessRefreshTalkAfterSendTalk(response.listTalk)
                    }
                })
    }

    override fun sendComment(talkId: String, message: String,
                             attachedProduct: List<TalkProductAttachmentViewModel>) {
        view.showLoadingAction()
        sendCommentsUseCase.execute(SendCommentsUseCase.getParameters(
                talkId,
                attachedProduct,
                message),
                object : Subscriber<SendCommentResponse>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        view.onError(e)
                    }

                    override fun onNext(response: SendCommentResponse) {
                        view.onSuccessSendTalkComment(talkId, response.comment_id)
                    }
                })
    }

    override fun deleteTalk(shopId: String, talkId: String) {
        view.showLoadingAction()

        deleteTalkUseCase.execute(DeleteTalkUseCase.getParam(
                shopId,
                talkId
        ), object : Subscriber<BaseActionTalkViewModel>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                view.onErrorActionTalk(e)
            }

            override fun onNext(talkViewModel: BaseActionTalkViewModel) {
                view.hideLoadingAction()
                view.onSuccessDeleteTalk(talkId)
            }
        })
    }

    override fun deleteCommentTalk(shopId: String, talkId: String, commentId: String) {
        view.showLoadingAction()
        deleteCommentTalkUseCase.execute(DeleteCommentTalkUseCase.getParam(
                shopId,
                talkId,
                commentId
        ), object : Subscriber<BaseActionTalkViewModel>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                view.onErrorActionTalk(e)

            }

            override fun onNext(talkViewModel: BaseActionTalkViewModel) {
                view.hideLoadingAction()
                view.onSuccessDeleteCommentTalk(talkId, commentId)

            }
        })
    }

    override fun unfollowTalk(talkId: String) {
        view.showLoadingAction()
        followUnfollowTalkUseCase.execute(FollowUnfollowTalkUseCase.getParam(
                talkId
        ), object : Subscriber<BaseActionTalkViewModel>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                view.onErrorActionTalk(e)

            }

            override fun onNext(talkViewModel: BaseActionTalkViewModel) {
                view.hideLoadingAction()
                view.onSuccessUnfollowTalk(talkId)

            }
        })
    }

    override fun followTalk(talkId: String) {
        view.showLoadingAction()
        followUnfollowTalkUseCase.execute(FollowUnfollowTalkUseCase.getParam(
                talkId
        ), object : Subscriber<BaseActionTalkViewModel>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                view.onErrorActionTalk(e)

            }

            override fun onNext(talkViewModel: BaseActionTalkViewModel) {
                view.hideLoadingAction()
                view.onSuccessFollowTalk(talkId)
            }
        })
    }

    override fun markTalkNotFraud(talkId: String) {
        view.showLoadingAction()
        markTalkNotFraudUseCase.execute(MarkTalkNotFraudUseCase.getParamTalk(
                talkId
        ), object : Subscriber<BaseActionTalkViewModel>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                view.onErrorActionTalk(e)

            }

            override fun onNext(talkViewModel: BaseActionTalkViewModel) {
                view.hideLoadingAction()
                view.onSuccessMarkTalkNotFraud(talkId)
            }
        })
    }

    override fun markCommentNotFraud(talkId: String, commentId: String) {
        view.showLoadingAction()
        markTalkNotFraudUseCase.execute(MarkTalkNotFraudUseCase.getParamTalk(
                talkId
        ), object : Subscriber<BaseActionTalkViewModel>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                view.onErrorActionTalk(e)

            }

            override fun onNext(talkViewModel: BaseActionTalkViewModel) {
                view.hideLoadingAction()
                view.onSuccessMarkCommentNotFraud(talkId, commentId)
            }
        })
    }

    override fun detachView() {
        super.detachView()
        getTalkComments.unsubscribe()
        sendCommentsUseCase.unsubscribe()
        deleteTalkUseCase.unsubscribe()
        deleteCommentTalkUseCase.unsubscribe()
        followUnfollowTalkUseCase.unsubscribe()
        markTalkNotFraudUseCase.unsubscribe()
    }

}