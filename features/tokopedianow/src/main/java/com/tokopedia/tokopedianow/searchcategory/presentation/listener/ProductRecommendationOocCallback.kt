package com.tokopedia.tokopedianow.searchcategory.presentation.listener

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.tokopedianow.common.viewholder.TokoNowProductRecommendationOocViewHolder

class ProductRecommendationOocCallback(
    private val lifecycle: Lifecycle
) : TokoNowProductRecommendationOocViewHolder.TokonowRecomBindPageNameListener {
    override fun onMiniCartUpdatedFromRecomWidget(miniCartSimplifiedData: MiniCartSimplifiedData) {

    }

    override fun onRecomTokonowAtcSuccess(message: String) {

    }

    override fun onRecomTokonowAtcFailed(throwable: Throwable) {

    }

    override fun onRecomTokonowAtcNeedToSendTracker(
        recommendationItem: RecommendationItem
    ) {

    }

    override fun onRecomTokonowDeleteNeedToSendTracker(
        recommendationItem: RecommendationItem
    ) {

    }

    override fun onClickItemNonLoginState() {

    }

    override fun setViewToLifecycleOwner(observer: LifecycleObserver) {
        lifecycle.addObserver(observer)
    }
}
