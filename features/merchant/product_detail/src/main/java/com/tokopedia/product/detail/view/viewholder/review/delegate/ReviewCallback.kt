package com.tokopedia.product.detail.view.viewholder.review.delegate

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.view.componentization.PdpComponentCallbackMediator
import com.tokopedia.product.detail.view.fragment.delegate.BaseComponentCallback
import com.tokopedia.product.detail.view.viewholder.review.event.OnKeywordClicked
import com.tokopedia.product.detail.view.viewholder.review.event.ReviewComponentEvent

/**
 * Created by yovi.putra on 14/11/23"
 * Project name: android-tokopedia-core
 **/

class ReviewCallback(
    private val mediator: PdpComponentCallbackMediator
) : BaseComponentCallback<ReviewComponentEvent>(mediator = mediator) {

    override fun onEvent(event: ReviewComponentEvent) {
        when (event) {
            is OnKeywordClicked -> {
                val pid = viewModel.getDynamicProductInfoP1?.basic?.productID.orEmpty()
                goToReviewDetail(productId = pid, topic = event.keyword)
            }
        }
    }

    private fun goToReviewDetail(productId: String, topic: String) {
        val context = context ?: return
        RouteManager.getIntent(context, ApplinkConst.PRODUCT_REVIEW, productId).apply {
            putExtra(ProductDetailConstant.REVIEW_PRD_NM, topic)
        }.also {
            context.startActivity(it)
        }
    }
}
