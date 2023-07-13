package com.tokopedia.product.detail.postatc.view.component.recommendation

import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.product.detail.postatc.base.BaseCallbackImpl
import com.tokopedia.product.detail.postatc.tracker.RecommendationTracking
import com.tokopedia.product.detail.postatc.view.PostAtcBottomSheet
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

interface RecommendationCallback {
    fun fetchRecommendation(pageName: String, uniqueId: Int)
    fun onClickRecommendationItem(recommendationItem: RecommendationItem)
    fun onImpressRecommendationItem(recommendationItem: RecommendationItem)
}

class RecommendationCallbackImpl(
    fragment: PostAtcBottomSheet
) : BaseCallbackImpl(fragment), RecommendationCallback {
    override fun fetchRecommendation(
        pageName: String,
        uniqueId: Int
    ) {
        val fragment = fragment ?: return
        val productId = fragment.viewModel.postAtcInfo.productId
        fragment.viewModel.fetchRecommendation(productId, pageName, uniqueId)
    }

    override fun onClickRecommendationItem(
        recommendationItem: RecommendationItem
    ) {
        val fragment = fragment ?: return
        val productId = recommendationItem.productId.toString()
        val userSession = fragment.userSession
        RecommendationTracking.onClickProductCard(
            fragment.viewModel.postAtcInfo.productId,
            userSession.userId,
            userSession.isLoggedIn,
            recommendationItem,
            fragment.trackingQueue
        )
        onClickProduct(fragment, productId)
    }

    override fun onImpressRecommendationItem(
        recommendationItem: RecommendationItem
    ) {
        val fragment = fragment ?: return
        val userSession = fragment.userSession
        RecommendationTracking.onImpressionProductCard(
            fragment.viewModel.postAtcInfo.productId,
            userSession.userId,
            userSession.isLoggedIn,
            recommendationItem,
            fragment.trackingQueue
        )
    }

    private fun onClickProduct(fragment: PostAtcBottomSheet, productId: String) = with(fragment) {
        RouteManager.route(
            context,
            ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
            productId
        )
        dismiss()
    }
}
