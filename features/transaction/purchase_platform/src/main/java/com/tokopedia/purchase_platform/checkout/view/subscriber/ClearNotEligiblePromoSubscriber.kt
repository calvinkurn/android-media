package com.tokopedia.purchase_platform.checkout.view.subscriber

import com.tokopedia.purchase_platform.checkout.view.ShipmentContract
import com.tokopedia.purchase_platform.checkout.view.ShipmentPresenter
import com.tokopedia.purchase_platform.checkout.view.viewmodel.NotEligiblePromoHolderdata
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