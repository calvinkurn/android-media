package com.tokopedia.promocheckout.detail.view.presenter

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

interface PromoCheckoutDetailEventContract {
    interface Presenter : CustomerPresenter<PromoCheckoutDetailContract.View> {
        fun getDetailPromo(slug: String)
        fun checkVoucher(promoCode: String)
    }
}