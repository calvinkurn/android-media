package com.tokopedia.promocheckout.list.view.presenter

import android.content.res.Resources
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.promocheckout.common.data.entity.request.Promo
import com.tokopedia.promocheckout.common.view.uimodel.ClashingInfoDetailUiModel

interface PromoCheckoutListMarketplaceContract {

    interface View : PromoCheckoutListContract.View{
        fun onClashCheckPromo(clasingInfoDetailUiModel: ClashingInfoDetailUiModel)
    }

    interface Presenter : CustomerPresenter<View>{
        fun checkPromoStackingCode(promoCode: String, oneClickShipment: Boolean, promo: Promo?)
        fun getListExchangeCoupon(resources: Resources)
    }
}