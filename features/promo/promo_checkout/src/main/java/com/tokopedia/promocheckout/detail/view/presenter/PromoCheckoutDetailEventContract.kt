package com.tokopedia.promocheckout.detail.view.presenter

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.promocheckout.common.domain.model.event.EventVerifyBody

interface PromoCheckoutDetailEventContract {
    interface Presenter : CustomerPresenter<PromoCheckoutDetailContract.View> {
        fun getDetailPromo(slug: String)
        fun checkPromoCode(promoCode: String, book: Boolean, eventVerifyBody: EventVerifyBody)    }
}