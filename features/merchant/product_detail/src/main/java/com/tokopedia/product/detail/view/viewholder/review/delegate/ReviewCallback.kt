package com.tokopedia.product.detail.view.viewholder.review.delegate

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.view.componentization.PdpComponentCallbackMediator
import com.tokopedia.product.detail.view.fragment.delegate.BaseComponentCallback
import com.tokopedia.product.detail.view.viewholder.review.event.OnKeywordClicked
import com.tokopedia.product.detail.view.viewholder.review.event.ReviewComponentEvent
import com.tokopedia.product.detail.view.viewholder.review.tracker.ReviewTracker

/**
 * Created by yovi.putra on 14/11/23"
 * Project name: android-tokopedia-core
 **/

class ReviewCallback(
    mediator: PdpComponentCallbackMediator
) : BaseComponentCallback<ReviewComponentEvent>(mediator = mediator) {

    override fun onEvent(event: ReviewComponentEvent) {
        when (event) {
            is OnKeywordClicked -> onKeywordClicked(event = event)
        }
    }

    private fun onKeywordClicked(event: OnKeywordClicked) {
        // action
        val pid = viewModel.getDynamicProductInfoP1?.basic?.productID.orEmpty()
        goToReviewDetail(productId = pid, keyword = event.keyword)

        // tracker
        val tracker = createCommonTracker(componentTracker = event.trackerData) ?: return
        ReviewTracker.onKeywordClicked(
            queueTracker = queueTracker,
            trackerData = tracker,
            count = event.keywordAmount
        )
    }

    private fun goToReviewDetail(productId: String, keyword: String) {
        val context = context ?: return
        RouteManager.getIntent(context, ApplinkConst.PRODUCT_REVIEW, productId).apply {
            putExtra(ProductDetailConstant.REVIEW_PRD_NM, keyword)
        }.also {
            context.startActivity(it)
        }
    }
}
