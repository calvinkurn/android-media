package com.tokopedia.checkout.view.feature.cartlist.subscriber

import com.tokopedia.checkout.view.feature.cartlist.ICartListPresenter
import com.tokopedia.checkout.view.feature.cartlist.ICartListView
import com.tokopedia.graphql.data.model.GraphqlResponse
import rx.Subscriber

/**
 * Created by Irfan Khoirul on 25/03/19.
 */

class CheckPromoFirstStepAfterClashSubscriber(val view: ICartListView?,
                                              val presenter: ICartListPresenter,
                                              val promoListSize: Int,
                                              val currentPromoIndex: Int) : Subscriber<GraphqlResponse>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        if (currentPromoIndex == promoListSize - 1) {
            view?.hideProgressLoading()
            presenter.processInitialGetCartData(false)
        }
    }

    override fun onNext(response: GraphqlResponse) {
        if (currentPromoIndex == promoListSize - 1) {
            view?.hideProgressLoading()
            presenter.processInitialGetCartData(false)
        }
    }

}