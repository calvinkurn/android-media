package com.tokopedia.promocheckout.list.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter

class PromoCheckoutListEventPresenter():BaseDaggerPresenter<PromoCheckoutListContract.View>(),
        PromoCheckoutListEventContract.Presenter{

    override fun checkPromoCode(promoCode: String) {

    }
}