package com.tokopedia.promocheckout.detail.view.presenter

import android.content.res.Resources
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.promocheckout.common.domain.model.DataVoucher
import com.tokopedia.promocheckout.detail.model.PromoCheckoutDetailModel

interface PromoCheckoutDetailContract {
    interface View : CustomerView{
        fun onErroGetDetail(e: Throwable)
        fun onSuccessGetDetailPromo(promoCheckoutDetailModel: PromoCheckoutDetailModel)
        fun onSuccessValidatePromo(dataVoucher: DataVoucher)
        fun onErrorValidatePromo(e: Throwable)
        fun showLoading()
        fun hideLoading()
        fun onErrorCancelPromo(e: Throwable)
        fun onSuccessCancelPromo()
        fun showProgressLoading()
        fun hideProgressLoading()
    }

    interface Presenter : CustomerPresenter<View>{
        fun getDetailPromo(codeCoupon: String, oneClickShipment: Boolean)
        fun validatePromoUse(codeCoupon: String, oneClickShipment : Boolean, resources: Resources)
        fun cancelPromo()
    }
}