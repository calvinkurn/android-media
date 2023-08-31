package com.tokopedia.product.detail.postatc.view.component.recommendation

import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.product.detail.postatc.base.PostAtcBottomSheetDelegate
import com.tokopedia.product.detail.postatc.tracker.RecommendationTracking
import com.tokopedia.product.detail.postatc.view.PostAtcBottomSheet
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

interface RecommendationCallback {
    fun fetchRecommendation(pageName: String, uniqueId: Int, queryParam: String)
    fun onClickRecommendationItem(recommendationItem: RecommendationItem)
    fun onImpressRecommendationItem(recommendationItem: RecommendationItem)
}

class RecommendationCallbackImpl(
    fragment: PostAtcBottomSheet
) : RecommendationCallback, PostAtcBottomSheetDelegate by fragment {
    override fun fetchRecommendation(
        pageName: String,
        uniqueId: Int,
        queryParam: String
    ) {
        viewModel.fetchRecommendation(
            viewModel.postAtcInfo.productId,
            pageName,
            uniqueId,
            queryParam
        )
    }

    override fun onClickRecommendationItem(
        recommendationItem: RecommendationItem
    ) {
        val productId = recommendationItem.productId.toString()
        RecommendationTracking.onClickProductCard(
            viewModel.postAtcInfo.productId,
            userSession.userId,
            userSession.isLoggedIn,
            recommendationItem,
            trackingQueue
        )
        onClickProduct(productId)
    }

    override fun onImpressRecommendationItem(
        recommendationItem: RecommendationItem
    ) {
        RecommendationTracking.onImpressionProductCard(
            viewModel.postAtcInfo.productId,
            userSession.userId,
            userSession.isLoggedIn,
            recommendationItem,
            trackingQueue
        )
    }

    private fun onClickProduct(productId: String) {
        val context = getContext() ?: return
        RouteManager.route(
            context,
            ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
            productId
        )
        dismiss()
    }
}
