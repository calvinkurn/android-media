package com.tokopedia.purchase_platform.features.cart.view.subscriber

import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.purchase_platform.common.data.api.CartResponseErrorException
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartListData
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.ShopGroupAvailableData
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.UpdateCartData
import com.tokopedia.purchase_platform.features.cart.view.ICartListPresenter
import com.tokopedia.purchase_platform.features.cart.view.ICartListView
import rx.Subscriber

/**
 * Created by Irfan Khoirul on 2019-12-18.
 */

class UpdateCartPromoMerchantSubscriber(private val view: ICartListView?,
                                        private val presenter: ICartListPresenter?,
                                        private val cartListData: CartListData?,
                                        private val shopGroupAvailableData: ShopGroupAvailableData) : Subscriber<UpdateCartData>() {
    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        view?.let {
            it.hideProgressLoading()
            var errorMessage = e.message
            if (e !is CartResponseErrorException) {
                errorMessage = ErrorHandler.getErrorMessage(it.getActivityObject(), e)
            }
            it.showToastMessageRed(errorMessage ?: "")
            // Todo : Remove this
            presenter?.processInitialGetCartData(it.getCartId(), cartListData == null, false)
        }
    }

    override fun onNext(data: UpdateCartData) {
        view?.let {
            it.hideProgressLoading()
            if (!data.isSuccess) {
                it.showToastMessageRed(data.message ?: "")
            } else {
                it.showMerchantVoucherListBottomsheet(shopGroupAvailableData)
            }
        }
    }
}