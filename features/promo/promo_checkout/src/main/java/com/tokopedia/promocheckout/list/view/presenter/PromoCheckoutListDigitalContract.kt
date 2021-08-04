package com.tokopedia.promocheckout.list.view.presenter

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.promocheckout.common.view.uimodel.PromoDigitalModel

@Deprecated("Not needed anymore.")
interface PromoCheckoutListDigitalContract {

    interface Presenter : CustomerPresenter<PromoCheckoutListContract.View>{
        fun checkPromoCode(promoCode: String, promoDigitalModel: PromoDigitalModel)
    }
}