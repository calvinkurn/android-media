package com.tokopedia.product.detail.view.viewholder.review.delegate

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.product.detail.view.componentization.PdpComponentCallbackMediator
import com.tokopedia.product.detail.view.fragment.delegate.BaseComponentCallback
import com.tokopedia.product.detail.view.viewholder.review.event.ReviewComponentEvent
import com.tokopedia.product.detail.view.viewholder.review.tracker.ReviewTracker
import com.tokopedia.reviewcommon.constant.ReviewCommonConstants

/**
 * Created by yovi.putra on 14/11/23"
 * Project name: android-tokopedia-core
 **/

class ReviewCallback(
    mediator: PdpComponentCallbackMediator
) : BaseComponentCallback<ReviewComponentEvent>(mediator = mediator) {

    override fun onEvent(event: ReviewComponentEvent) {
        when (event) {
            is ReviewComponentEvent.OnKeywordClicked -> onKeywordClicked(event = event)
            is ReviewComponentEvent.OnKeywordImpressed -> onKeywordImpressed(event = event)
        }
    }

    // region rating keyword
    private fun onKeywordImpressed(event: ReviewComponentEvent.OnKeywordImpressed) {
        val commonTracker = getCommonTracker() ?: return
        ReviewTracker.onKeywordImpressed(
            queueTracker = queueTracker,
            commonTracker = commonTracker,
            componentTracker = event.trackerData,
            count = event.keywordAmount
        )
    }

    private fun onKeywordClicked(event: ReviewComponentEvent.OnKeywordClicked) {
        // action
        val pid = viewModel.getProductInfoP1?.basic?.productID.orEmpty()
        goToReviewDetail(productId = pid, keyword = event.keyword)

        // tracker
        val commonTracker = getCommonTracker() ?: return
        ReviewTracker.onKeywordClicked(
            queueTracker = queueTracker,
            commonTracker = commonTracker,
            componentTracker = event.trackerData,
            count = event.keywordAmount
        )
    }
    // endregion

    private fun goToReviewDetail(productId: String, keyword: String) {
        val context = context ?: return
        RouteManager.getIntent(context, ApplinkConst.PRODUCT_REVIEW, productId).apply {
            putExtra(ReviewCommonConstants.EXTRAS_SELECTED_TOPIC, keyword)
        }.also {
            context.startActivity(it)
        }
    }
}
