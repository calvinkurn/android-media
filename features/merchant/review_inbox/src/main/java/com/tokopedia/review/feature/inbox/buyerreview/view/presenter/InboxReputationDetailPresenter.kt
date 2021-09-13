package com.tokopedia.review.feature.inbox.buyerreview.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inboxdetail.DeleteReviewResponseUseCase
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inboxdetail.GetInboxReputationDetailUseCase
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inboxdetail.SendReplyReviewUseCase
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inboxdetail.SendSmileyReputationUseCase
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputationDetail
import com.tokopedia.review.feature.inbox.buyerreview.view.subscriber.*
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by nisie on 8/19/17.
 */
class InboxReputationDetailPresenter @Inject internal constructor(
    private val getInboxReputationDetailUseCase: GetInboxReputationDetailUseCase,
    private val sendSmileyReputationUseCase: SendSmileyReputationUseCase,
    private val deleteReviewResponseUseCase: DeleteReviewResponseUseCase,
    private val sendReplyReviewUseCase: SendReplyReviewUseCase,
    private val userSession: UserSessionInterface
) : BaseDaggerPresenter<InboxReputationDetail.View?>(), InboxReputationDetail.Presenter {
    private var viewListener: InboxReputationDetail.View? = null
    public override fun attachView(view: InboxReputationDetail.View) {
        super.attachView(view)
        viewListener = view
    }

    public override fun detachView() {
        super.detachView()
        getInboxReputationDetailUseCase.unsubscribe()
        sendSmileyReputationUseCase.unsubscribe()
        deleteReviewResponseUseCase.unsubscribe()
        sendReplyReviewUseCase.unsubscribe()
    }

    public override fun getInboxDetail(reputationId: String?, tab: Int) {
        viewListener!!.showLoading()
        getInboxReputationDetailUseCase.execute(
            GetInboxReputationDetailUseCase.Companion.getParam(
                reputationId,
                userSession.getUserId(),
                tab
            ),
            GetInboxReputationDetailSubscriber(viewListener)
        )
    }

    public override fun sendSmiley(reputationId: String?, score: String?, role: Int) {
        viewListener!!.showLoadingDialog()
        sendSmileyReputationUseCase.execute(
            SendSmileyReputationUseCase.Companion.getParam(
                reputationId,
                score,
                role
            ),
            SendSmileySubscriber(viewListener, score)
        )
    }

    public override fun deleteReviewResponse(
        reviewId: String?,
        productId: String?,
        shopId: String?,
        reputationId: String?
    ) {
        viewListener!!.showLoadingDialog()
        deleteReviewResponseUseCase.execute(
            DeleteReviewResponseUseCase.Companion.getParam(
                reviewId,
                productId,
                shopId,
                reputationId
            ), DeleteReviewResponseSubscriber(viewListener)
        )
    }

    public override fun sendReplyReview(
        reputationId: Long, productId: String?, shopId: Long,
        reviewId: String?, replyReview: String?
    ) {
        viewListener!!.showLoadingDialog()
        sendReplyReviewUseCase.execute(
            SendReplyReviewUseCase.Companion.getParam(
                reputationId.toString(),
                productId, shopId.toString(),
                reviewId,
                replyReview
            ), ReplyReviewSubscriber(viewListener)
        )
    }

    fun refreshPage(reputationId: String?, tab: Int) {
        viewListener!!.showRefresh()
        getInboxReputationDetailUseCase.execute(
            GetInboxReputationDetailUseCase.Companion.getParam(
                reputationId,
                userSession.getUserId(),
                tab
            ),
            RefreshInboxReputationDetailSubscriber(viewListener)
        )
    }
}