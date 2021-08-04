package com.tokopedia.promocheckout.detail.view.presenter

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

@Deprecated("Not needed anymore.")
interface PromoCheckoutDetailHotelContract {
    interface Presenter : CustomerPresenter<PromoCheckoutDetailContract.View>{
        fun getDetailPromo(codeCoupon: String)
        fun checkVoucher(promoCode: String, cartID: String, hexColor: String)
        fun cancelPromo()
    }
}