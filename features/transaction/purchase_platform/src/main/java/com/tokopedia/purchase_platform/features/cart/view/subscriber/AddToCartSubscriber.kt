package com.tokopedia.purchase_platform.features.cart.view.subscriber

import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.purchase_platform.features.cart.view.ICartListPresenter
import com.tokopedia.purchase_platform.features.cart.view.ICartListView
import com.tokopedia.purchase_platform.common.data.api.CartResponseErrorException
import rx.Subscriber

/**
 * Created by Irfan Khoirul on 21/03/19.
 */

class AddToCartSubscriber(val view: ICartListView?,
                          val presenter: ICartListPresenter,
                          val productModel: Any) : Subscriber<AddToCartDataModel>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        if (view != null) {
            view.hideProgressLoading()
            var errorMessage = e.message
            if (e !is CartResponseErrorException) {
                errorMessage = ErrorHandler.getErrorMessage(view.getActivityObject(), e)
            }
            view.showToastMessageRed(errorMessage ?: "")
        }
    }

    override fun onNext(addToCartDataModel: AddToCartDataModel) {
        if (view != null) {
            view.hideProgressLoading()
            if (addToCartDataModel.status.equals(AddToCartDataModel.STATUS_OK, true) && addToCartDataModel.data.success == 1) {
                view.triggerSendEnhancedEcommerceAddToCartSuccess(addToCartDataModel, productModel)
                presenter.processInitialGetCartData("0", false, false)
                if (addToCartDataModel.data.message.size > 0) {
                    view.showToastMessageGreen(addToCartDataModel.data.message[0])
                    view.notifyBottomCartParent();
                }
            } else {
                if (addToCartDataModel.errorMessage.size > 0) {
                    view.showToastMessageRed(addToCartDataModel.errorMessage[0])
                }
            }
        }
    }

}