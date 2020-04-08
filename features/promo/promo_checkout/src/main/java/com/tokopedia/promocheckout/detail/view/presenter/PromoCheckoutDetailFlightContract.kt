package com.tokopedia.promocheckout.detail.view.presenter

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

interface PromoCheckoutDetailFlightContract {
    interface Presenter : CustomerPresenter<PromoCheckoutDetailContract.View>{
        fun getDetailPromo(codeCoupon: String)
        fun checkVoucher(promoCode: String, cartID: String)
        fun cancelPromo()
    }
}