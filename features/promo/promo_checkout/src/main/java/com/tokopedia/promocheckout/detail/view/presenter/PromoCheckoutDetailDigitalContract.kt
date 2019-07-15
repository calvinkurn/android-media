package com.tokopedia.promocheckout.detail.view.presenter

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.promocheckout.common.data.entity.request.Promo
import com.tokopedia.promocheckout.common.view.uimodel.ClashingInfoDetailUiModel
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.promocheckout.detail.model.PromoCheckoutDetailModel

interface PromoCheckoutDetailDigitalContract: PromoCheckoutDetailContract{

    interface Presenter : CustomerPresenter<PromoCheckoutDetailContract.View>{
        fun getDetailPromo(codeCoupon: String, promo: Promo?)
        fun validatePromoStackingUse(promoCode: String, promo: Promo?, isFromLoadDetail: Boolean)
        fun cancelPromo(codeCoupon: String)
    }
}