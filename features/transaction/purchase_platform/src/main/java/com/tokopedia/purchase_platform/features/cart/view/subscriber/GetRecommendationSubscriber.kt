package com.tokopedia.purchase_platform.features.cart.view.subscriber

import com.tokopedia.purchase_platform.features.cart.view.ICartListPresenter
import com.tokopedia.purchase_platform.features.cart.view.ICartListView
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget

import rx.Subscriber

/**
 * Created by Irfan Khoirul on 20/09/18.
 */

class GetRecommendationSubscriber(private val view: ICartListView?,
                                  private val presenter: ICartListPresenter) : Subscriber<List<RecommendationWidget>>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        view?.hideItemLoading()
        view?.setHasTriedToLoadRecommendation()
        view?.stopAllCartPerformanceTrace()
    }

    override fun onNext(recommendationModels: List<RecommendationWidget>) {
        if (view != null) {
            view.hideItemLoading()
            if (recommendationModels[0].recommendationItemList.isNotEmpty()) {
                view.renderRecommendation(recommendationModels[0])
            }
            view.setHasTriedToLoadRecommendation()
            view.stopAllCartPerformanceTrace()
        }
    }
}
