package com.tokopedia.cart.view.subscriber

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.cart.view.ICartListPresenter
import com.tokopedia.cart.view.ICartListView
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
            view.showToastMessageRed(e)
        }
    }

    override fun onNext(addToCartDataModel: AddToCartDataModel) {
        if (view != null) {
            view.hideProgressLoading()
            if (addToCartDataModel.status.equals(AddToCartDataModel.STATUS_OK, true) && addToCartDataModel.data.success == 1) {
                view.triggerSendEnhancedEcommerceAddToCartSuccess(addToCartDataModel, productModel)
                view.resetRecentViewList()
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