package com.tokopedia.checkout.view.feature.cartlist.subscriber

import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.checkout.view.feature.cartlist.ICartListPresenter
import com.tokopedia.checkout.view.feature.cartlist.ICartListView
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.promocheckout.common.domain.model.clearpromo.ClearCacheAutoApplyStackResponse
import com.tokopedia.transactiondata.apiservice.CartResponseErrorException
import com.tokopedia.transactiondata.entity.response.addtocart.AddToCartDataResponse
import rx.Subscriber

/**
 * Created by Irfan Khoirul on 21/03/19.
 */

class AddToCartSubscriber(val view: ICartListView?,
                          val presenter: ICartListPresenter) : Subscriber<AddToCartDataResponse>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        if (view != null) {
            view.hideProgressLoading()
            var errorMessage = e.message
            if (e !is CartResponseErrorException) {
                errorMessage = ErrorHandler.getErrorMessage(view.activity, e)
            }
            view.showToastMessageRed(errorMessage)
        }
    }

    override fun onNext(response: AddToCartDataResponse) {
        if (view != null) {
            view.hideProgressLoading()
            if (response.success == 1) {
                presenter.processInitialGetCartData("0", false)
                view.showToastMessageGreen(response.message[0])
            } else {
                view.showToastMessageRed(response.message[0])
            }
        }
    }

}