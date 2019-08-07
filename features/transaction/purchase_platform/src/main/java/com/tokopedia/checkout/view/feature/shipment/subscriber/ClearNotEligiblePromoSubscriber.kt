package com.tokopedia.checkout.view.feature.shipment.subscriber

import com.tokopedia.checkout.view.feature.shipment.ShipmentContract
import com.tokopedia.checkout.view.feature.shipment.ShipmentPresenter
import com.tokopedia.checkout.view.feature.shipment.viewmodel.NotEligiblePromoHolderdata
import com.tokopedia.graphql.data.model.GraphqlResponse
import rx.Subscriber
import java.util.ArrayList

/**
 * Created by Irfan Khoirul on 2019-06-25.
 */

class ClearNotEligiblePromoSubscriber(val view: ShipmentContract.View?,
                                      val presenter: ShipmentPresenter,
                                      val checkoutType: Int,
                                      val notEligiblePromoHolderdata: ArrayList<NotEligiblePromoHolderdata>) : Subscriber<GraphqlResponse>() {

    override fun onCompleted() {
    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        view?.hideLoading()
        view?.removeIneligiblePromo(checkoutType, notEligiblePromoHolderdata)
    }

    override fun onNext(t: GraphqlResponse?) {
        view?.hideLoading()
        view?.removeIneligiblePromo(checkoutType, notEligiblePromoHolderdata)
    }

}