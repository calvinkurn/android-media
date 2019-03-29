package com.tokopedia.promocheckout.list.view.presenter

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.promocheckout.common.domain.model.DataVoucher

interface PromoCheckoutListMarketplaceContract {

    interface View : PromoCheckoutListContract.View{
        fun showProgressLoading()
        fun hideProgressLoading()
        fun onErrorCheckPromoCode(e: Throwable)
        fun onSuccessCheckPromoCode(dataVoucher: DataVoucher)
        fun onErrorEmptyPromoCode()
    }

    interface Presenter : CustomerPresenter<View>{
        fun checkPromoCode(promoCode: String, oneClickShipment: Boolean)
    }
}