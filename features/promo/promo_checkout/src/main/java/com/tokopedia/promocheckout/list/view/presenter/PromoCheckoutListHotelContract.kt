package com.tokopedia.promocheckout.list.view.presenter

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

/**Deprecated*/
interface PromoCheckoutListHotelContract {

    interface Presenter : CustomerPresenter<PromoCheckoutListContract.View>{
        fun checkPromoCode(cartID: String, voucherCode: String)
    }
}