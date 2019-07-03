package com.tokopedia.checkout.view.feature.cartlist.subscriber

import com.tokopedia.checkout.view.feature.cartlist.ICartListPresenter
import com.tokopedia.checkout.view.feature.cartlist.ICartListView
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
    }

    override fun onNext(recommendationModels: List<RecommendationWidget>) {
        if (view != null) {
            view.hideItemLoading()
            view.renderRecommendation(recommendationModels[0].recommendationItemList)
        }
    }
}
