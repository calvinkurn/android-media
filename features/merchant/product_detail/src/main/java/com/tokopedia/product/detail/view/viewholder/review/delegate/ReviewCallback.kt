package com.tokopedia.product.detail.view.viewholder.review.delegate

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.view.componentization.ComponentCallbackDelegate
import com.tokopedia.product.detail.view.componentization.PdpComponentCallbackMediator
import com.tokopedia.product.detail.view.viewholder.review.event.OnTopicClicked
import com.tokopedia.product.detail.view.viewholder.review.event.ReviewComponentEvent

/**
 * Created by yovi.putra on 14/11/23"
 * Project name: android-tokopedia-core
 **/

class ReviewCallback(
    private val mediator: PdpComponentCallbackMediator
) : ComponentCallbackDelegate<ReviewComponentEvent> {

    private val _context
        get() = mediator.rootView.context

    private val _viewModel
        get() = mediator.pdpViewModel

    override fun onEvent(event: ReviewComponentEvent) {
        when (event) {
            is OnTopicClicked -> {
                val pid = _viewModel.getDynamicProductInfoP1?.basic?.productID.orEmpty()
                goToReviewDetail(productId = pid, topic = event.topic)
            }
        }
    }

    private fun goToReviewDetail(productId: String, topic: String) {
        val context = _context ?: return
        RouteManager.getIntent(context, ApplinkConst.PRODUCT_REVIEW, productId).apply {
            putExtra(ProductDetailConstant.REVIEW_PRD_NM, topic)
        }.also {
            context.startActivity(it)
        }
    }
}
