package com.tokopedia.checkout.view.feature.cartlist.subscriber

import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.atc_common.data.model.response.AddToCartGqlResponse
import com.tokopedia.checkout.domain.datamodel.addtocart.AddToCartDataResponseModel
import com.tokopedia.checkout.domain.datamodel.addtocart.DataModel
import com.tokopedia.checkout.view.feature.cartlist.ICartListPresenter
import com.tokopedia.checkout.view.feature.cartlist.ICartListView
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.transactiondata.apiservice.CartResponseErrorException
import rx.Subscriber

/**
 * Created by Irfan Khoirul on 21/03/19.
 */

class AddToCartSubscriber(val view: ICartListView?,
                          val presenter: ICartListPresenter,
                          val productModel: Any) : Subscriber<GraphqlResponse>() {

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

    override fun onNext(response: GraphqlResponse) {
        if (view != null) {
            val addToCartGqlResponse = response.getData<AddToCartGqlResponse>(AddToCartGqlResponse::class.java)
            view.hideProgressLoading()
            if (addToCartGqlResponse.addToCartResponse.status == "OK" && addToCartGqlResponse.addToCartResponse.data.success == 1) {
                val dataModel = DataModel()
                dataModel.cartId = addToCartGqlResponse.addToCartResponse.data.cartId
                dataModel.customerId = addToCartGqlResponse.addToCartResponse.data.customerId
                dataModel.notes = addToCartGqlResponse.addToCartResponse.data.notes
                dataModel.productId = addToCartGqlResponse.addToCartResponse.data.productId
                dataModel.shopId = addToCartGqlResponse.addToCartResponse.data.shopId
                dataModel.quantity = addToCartGqlResponse.addToCartResponse.data.quantity

                val addToCartDataModel = AddToCartDataResponseModel()
                addToCartDataModel.success = addToCartGqlResponse.addToCartResponse.data.success
                addToCartDataModel.message = addToCartGqlResponse.addToCartResponse.data.message
                addToCartDataModel.data = dataModel

                view.triggerSendEnhancedEcommerceAddToCartSuccess(addToCartDataModel, productModel)
                presenter.processInitialGetCartData("0", false, false)
                if (addToCartGqlResponse.addToCartResponse.data.message.size > 0) {
                    view.showToastMessageGreen(addToCartGqlResponse.addToCartResponse.data.message[0])
                }
            } else {
                if (addToCartGqlResponse.addToCartResponse.errorMessage.size > 0) {
                    view.showToastMessageRed(addToCartGqlResponse.addToCartResponse.errorMessage[0])
                }
            }
        }
    }

}