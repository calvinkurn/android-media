package com.tokopedia.promocheckout.list.view.presenter

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.promocheckout.common.domain.model.event.EventVerifyBody

interface PromoCheckoutListEventContract {

    interface Presenter : CustomerPresenter<PromoCheckoutListContract.View>{
        fun checkPromoCode(promoCode: String, book: Boolean, eventVerifyBody: EventVerifyBody)
    }
}