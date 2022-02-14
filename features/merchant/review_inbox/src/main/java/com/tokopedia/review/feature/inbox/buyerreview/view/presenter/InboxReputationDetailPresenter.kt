package com.tokopedia.review.feature.inbox.buyerreview.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inboxdetail.DeleteReviewResponseUseCase
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inboxdetail.GetInboxReputationDetailUseCase
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inboxdetail.SendReplyReviewUseCase
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inboxdetail.SendSmileyReputationUseCase
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputationDetail
import com.tokopedia.review.feature.inbox.buyerreview.view.subscriber.DeleteReviewResponseSubscriber
import com.tokopedia.review.feature.inbox.buyerreview.view.subscriber.GetInboxReputationDetailSubscriber
import com.tokopedia.review.feature.inbox.buyerreview.view.subscriber.RefreshInboxReputationDetailSubscriber
import com.tokopedia.review.feature.inbox.buyerreview.view.subscriber.ReplyReviewSubscriber
import com.tokopedia.review.feature.inbox.buyerreview.view.subscriber.SendSmileySubscriber
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
) : BaseDaggerPresenter<InboxReputationDetail.View>(), InboxReputationDetail.Presenter {

    private var viewListener: InboxReputationDetail.View? = null

    override fun attachView(view: InboxReputationDetail.View?) {
        super.attachView(view)
        viewListener = view
    }

    override fun detachView() {
        super.detachView()
        getInboxReputationDetailUseCase.unsubscribe()
        sendSmileyReputationUseCase.unsubscribe()
        deleteReviewResponseUseCase.unsubscribe()
        sendReplyReviewUseCase.unsubscribe()
    }

    override fun getInboxDetail(id: String, anInt: Int) {
        viewListener?.showLoading()
        getInboxReputationDetailUseCase.execute(
            GetInboxReputationDetailUseCase.getParam(
                id,
                userSession.userId,
                anInt
            ),
            viewListener?.let { GetInboxReputationDetailSubscriber(it) }
        )
    }

    override fun sendSmiley(reputationId: String, score: String, role: Int) {
        viewListener?.showLoadingDialog()
        sendSmileyReputationUseCase.execute(
            SendSmileyReputationUseCase.getParam(
                reputationId,
                score,
                role
            ),
            viewListener?.let { SendSmileySubscriber(it, score) }
        )
    }

    override fun deleteReviewResponse(
        reviewId: String,
        productId: String,
        shopId: String,
        reputationId: String
    ) {
        viewListener?.showLoadingDialog()
        deleteReviewResponseUseCase.execute(
            DeleteReviewResponseUseCase.getParam(
                reviewId,
                productId,
                shopId,
                reputationId
            ), viewListener?.let { DeleteReviewResponseSubscriber(it) }
        )
    }

    override fun sendReplyReview(
        reputationId: Long, productId: String, shopId: Long,
        reviewId: String, replyReview: String
    ) {
        viewListener?.showLoadingDialog()
        sendReplyReviewUseCase.execute(
            SendReplyReviewUseCase.getParam(
                reputationId.toString(),
                productId, shopId.toString(),
                reviewId,
                replyReview
            ), viewListener?.let { ReplyReviewSubscriber(it) }
        )
    }

    fun refreshPage(reputationId: String, tab: Int) {
        viewListener?.showRefresh()
        getInboxReputationDetailUseCase.execute(
            GetInboxReputationDetailUseCase.getParam(
                reputationId,
                userSession.userId,
                tab
            ),
            viewListener?.let { RefreshInboxReputationDetailSubscriber(it) }
        )
    }
}