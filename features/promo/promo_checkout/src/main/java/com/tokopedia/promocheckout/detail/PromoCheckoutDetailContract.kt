package com.tokopedia.promocheckout.detail

import android.content.res.Resources
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.promocheckout.detail.model.PromoCheckoutDetailModel

interface PromoCheckoutDetailContract {
    interface View : CustomerView{
        fun onErroGetDetail(e: Throwable)
        fun onSuccessGetDetailPromo(promoCheckoutDetailModel: PromoCheckoutDetailModel?)

    }

    interface Presenter : CustomerPresenter<View>{
        fun getDetailPromo(codeCoupon: String, resources: Resources)
    }
}