package com.tokopedia.promocheckout.list.view.presenter

import android.content.res.Resources
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.promocheckout.common.data.entity.request.Promo
import com.tokopedia.promocheckout.common.view.uimodel.ClashingInfoDetailUiModel
import com.tokopedia.promocheckout.list.model.listpromocatalog.TokopointsCatalogHighlight
import com.tokopedia.promocheckout.list.model.listpromolastseen.GetPromoSuggestion

interface PromoCheckoutListMarketplaceContract {

    interface View : PromoCheckoutListContract.View{
        fun renderListExchangeCoupon(data: TokopointsCatalogHighlight)
        fun renderListLastSeen(data: GetPromoSuggestion?)
        fun showListCatalogHighlight(e: Throwable)
        fun showProgressBar()
        fun hideProgressBar()
        fun onClashCheckPromo(clasingInfoDetailUiModel: ClashingInfoDetailUiModel)
    }

    interface Presenter : CustomerPresenter<View>{
        fun getListLastSeen(serviceId: String, resources: Resources)
        fun checkPromoStackingCode(promoCode: String, oneClickShipment: Boolean, promo: Promo?)
        fun getListExchangeCoupon(resources: Resources)
    }
}