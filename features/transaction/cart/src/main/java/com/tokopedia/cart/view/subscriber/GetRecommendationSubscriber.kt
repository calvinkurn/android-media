package com.tokopedia.cart.view.subscriber

import com.tokopedia.cart.view.ICartListView
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import rx.Subscriber

/**
 * Created by Irfan Khoirul on 20/09/18.
 */

class GetRecommendationSubscriber(private val view: ICartListView?) : Subscriber<List<RecommendationWidget>>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        view?.hideItemLoading()
        view?.setHasTriedToLoadRecommendation()
        view?.stopAllCartPerformanceTrace()
    }

    override fun onNext(recommendationModels: List<RecommendationWidget>) {
        view?.let {
            it.hideItemLoading()
            if (recommendationModels[0].recommendationItemList.isNotEmpty()) {
                it.renderRecommendation(recommendationModels[0])
            }
            it.setHasTriedToLoadRecommendation()
            it.stopAllCartPerformanceTrace()
        }
    }
}
