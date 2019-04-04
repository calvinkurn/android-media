package com.tokopedia.promocheckout.detail.view.presenter

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.promocheckout.common.data.entity.request.Promo
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.promocheckout.detail.model.PromoCheckoutDetailModel

interface PromoCheckoutDetailContract {
    interface View : CustomerView{
        fun onErroGetDetail(e: Throwable)
        fun onSuccessGetDetailPromo(promoCheckoutDetailModel: PromoCheckoutDetailModel)
        fun onSuccessValidatePromoStacking(data: DataUiModel)
        fun onErrorValidatePromo(e: Throwable)
        fun showLoading()
        fun hideLoading()
        fun onErrorCancelPromo(e: Throwable)
        fun onSuccessCancelPromoStacking()
        fun showProgressLoading()
        fun hideProgressLoading()
    }

    interface Presenter : CustomerPresenter<View>{
        fun getDetailPromo(codeCoupon: String, oneClickShipment: Boolean, promo: Promo?)
        fun validatePromoStackingUse(promoCode: String, promo: Promo?, isFromLoadDetail: Boolean)
        fun cancelPromo(codeCoupon: String)
    }
}