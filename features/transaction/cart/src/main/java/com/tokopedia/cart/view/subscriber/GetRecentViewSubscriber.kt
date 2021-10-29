package com.tokopedia.cart.view.subscriber

import com.tokopedia.cart.view.ICartListView
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import rx.Subscriber

/**
 * Created by Irfan Khoirul on 21/09/18.
 */

class GetRecentViewSubscriber(private val view: ICartListView?) : Subscriber<List<RecommendationWidget>>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        view?.setHasTriedToLoadRecentView()
        view?.stopAllCartPerformanceTrace()
    }

    override fun onNext(recommendationModels: List<RecommendationWidget>) {
        view?.let {
            it.hideItemLoading()
            if (recommendationModels.firstOrNull()?.recommendationItemList?.isNotEmpty() == true) {
                it.renderRecentView(recommendationModels[0])
            }
            it.setHasTriedToLoadRecentView()
            it.stopAllCartPerformanceTrace()
        }
    }

}
