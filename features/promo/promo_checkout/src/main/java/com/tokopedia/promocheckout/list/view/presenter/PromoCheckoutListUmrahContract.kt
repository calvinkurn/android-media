package com.tokopedia.promocheckout.list.view.presenter

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

interface PromoCheckoutListUmrahContract {
    interface Presenter : CustomerPresenter<PromoCheckoutListContract.View> {
        fun checkPromo(promoCode: String, totalPrice:Int)
    }
}