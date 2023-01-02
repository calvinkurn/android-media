package com.tokopedia.tokopedianow.repurchase.presentation.listener

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.tokopedianow.common.viewholder.TokoNowProductRecommendationOocViewHolder

class ProductRecommendationOocCallback(
    private val activity: FragmentActivity?,
    private val lifecycle: Lifecycle
): TokoNowProductRecommendationOocViewHolder.TokonowRecomBindPageNameListener {

    override fun onClickItemNonLoginState() {
        RouteManager.route(activity, ApplinkConst.LOGIN)
    }

    override fun setViewToLifecycleOwner(observer: LifecycleObserver) {
        lifecycle.addObserver(observer)
    }

    override fun onRecomTokonowDeleteNeedToSendTracker(
        recommendationItem: RecommendationItem
    ) { /* nothing to do */ }

    override fun onMiniCartUpdatedFromRecomWidget(
        miniCartSimplifiedData: MiniCartSimplifiedData
    ) { /* nothing to do */ }

    override fun onRecomTokonowAtcSuccess(
        message: String
    ) { /* nothing to do */ }

    override fun onRecomTokonowAtcFailed(
        throwable: Throwable
    ) { /* nothing to do */ }

    override fun onRecomTokonowAtcNeedToSendTracker(
        recommendationItem: RecommendationItem
    ) { /* nothing to do */ }
}
