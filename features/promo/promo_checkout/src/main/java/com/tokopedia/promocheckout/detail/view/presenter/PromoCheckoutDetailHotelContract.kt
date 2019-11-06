package com.tokopedia.promocheckout.detail.view.presenter

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.promocheckout.common.view.uimodel.PromoDigitalModel

interface PromoCheckoutDetailHotelContract {
    interface Presenter : CustomerPresenter<PromoCheckoutDetailContract.View>{
        fun getDetailPromo(codeCoupon: String)
        fun checkVoucher(promoCode: String, cartID: String)
        fun cancelPromo()
    }
}