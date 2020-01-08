package com.tokopedia.purchase_platform.features.cart.view.subscriber

import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.promocheckout.common.view.model.PromoStackingData
import com.tokopedia.purchase_platform.common.data.api.CartResponseErrorException
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartListData
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.UpdateCartData
import com.tokopedia.purchase_platform.features.cart.view.CartFragment
import com.tokopedia.purchase_platform.features.cart.view.ICartListPresenter
import com.tokopedia.purchase_platform.features.cart.view.ICartListView
import rx.Subscriber

/**
 * Created by Irfan Khoirul on 2019-12-18.
 */

class UpdateCartPromoGlobalSubscriber(private val view: ICartListView?,
                                      private val presenter: ICartListPresenter?,
                                      private val cartListData: CartListData?,
                                      private val promoStackingData: PromoStackingData,
                                      private val stateGoTo: Int) : Subscriber<UpdateCartData>() {
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
                if (stateGoTo == CartFragment.GO_TO_LIST) {
                    it.goToCouponList()
                } else {
                    it.goToDetailPromoStacking(promoStackingData)
                }
            }
        }
    }
}