package com.tokopedia.promocheckout.detail.view.presenter

import android.content.res.Resources
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.promocheckout.common.data.entity.request.CheckPromoFirstStepParam
import com.tokopedia.promocheckout.common.domain.model.DataVoucher
import com.tokopedia.promocheckout.common.domain.model.promostacking.response.Data
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.promocheckout.detail.model.PromoCheckoutDetailModel

interface PromoCheckoutDetailContract {
    interface View : CustomerView{
        fun onErroGetDetail(e: Throwable)
        fun onSuccessGetDetailPromo(promoCheckoutDetailModel: PromoCheckoutDetailModel)
        // fun onSuccessValidatePromo(dataVoucher: DataVoucher)
        fun onSuccessValidatePromoStacking(data: DataUiModel)
        fun onErrorValidatePromo(e: Throwable)
        fun showLoading()
        fun hideLoading()
        fun onErrorCancelPromo(e: Throwable)
        // fun onSuccessCancelPromo()
        fun onSuccessCancelPromoStacking()
        fun showProgressLoading()
        fun hideProgressLoading()
    }

    interface Presenter : CustomerPresenter<View>{
        fun getDetailPromo(codeCoupon: String, oneClickShipment: Boolean)
        // fun validatePromoUse(codeCoupon: String, oneClickShipment : Boolean, resources: Resources)
        fun validatePromoStackingUse(promoCode: String, checkPromoFirstStepParam: CheckPromoFirstStepParam?)
        fun cancelPromo()
    }
}