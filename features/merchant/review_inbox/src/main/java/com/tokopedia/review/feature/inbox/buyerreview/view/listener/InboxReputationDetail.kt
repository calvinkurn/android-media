package com.tokopedia.review.feature.inbox.buyerreview.view.listener

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.InboxReputationItemUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.InboxReputationDetailItemUiModel
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ProductrevGetReviewMedia

/**
 * @author by nisie on 8/19/17.
 */
interface InboxReputationDetail {
    interface View : CustomerView {
        fun showLoading()
        fun onErrorGetInboxDetail(throwable: Throwable)
        fun onSuccessGetInboxDetail(
            inboxReputationItemUiModel: InboxReputationItemUiModel,
            visitables: List<Visitable<*>>
        )

        fun finishLoading()
        fun onErrorSendSmiley(errorMessage: String?)
        fun showLoadingDialog()
        fun finishLoadingDialog()
        fun showRefresh()
        fun onErrorRefreshInboxDetail(throwable: Throwable)
        fun onSuccessRefreshGetInboxDetail(
            inboxReputationViewModel: InboxReputationItemUiModel,
            visitables: List<Visitable<*>>
        )

        fun finishRefresh()
        fun goToPreviewImage(position: Int, preloadedDetailedReviewMedia: ProductrevGetReviewMedia)
        val tab: Int
        fun getErrorMessage(throwable: Throwable = Throwable()): String
        fun onGoToReportReview(shopId: String, reviewId: String?)
        fun onSuccessSendSmiley(score: Int)
        fun onErrorFavoriteShop(errorMessage: String?)
        fun onSuccessFavoriteShop()
        fun onDeleteReviewResponse(element: InboxReputationDetailItemUiModel)
        fun onSendReplyReview(element: InboxReputationDetailItemUiModel, replyReview: String)
        fun onShareReview(
            inboxReputationDetailItemUiModel: InboxReputationDetailItemUiModel,
            adapterPosition: Int
        )

        fun onGoToProductDetail(productId: String?, productAvatar: String?, productName: String?)
        fun onSmoothScrollToReplyView(adapterPosition: Int)
        fun onGoToProfile(reviewerId: String)
        fun onGoToShopInfo(shopId: String)
        fun getShopId(): String
        fun onClickReviewOverflowMenu(
            inboxReputationDetailItemUiModel: InboxReputationDetailItemUiModel,
            adapterPosition: Int
        )

        fun onClickToggleReply(
            inboxReputationDetailItemUiModel: InboxReputationDetailItemUiModel,
            adapterPosition: Int
        )
    }

    interface Presenter : CustomerPresenter<View?> {
        fun getInboxDetail(id: String, anInt: Int)
        fun sendSmiley(reputationId: String, score: String, role: String)
    }
}