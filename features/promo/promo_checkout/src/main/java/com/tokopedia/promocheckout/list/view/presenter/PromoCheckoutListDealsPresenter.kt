package com.tokopedia.promocheckout.list.view.presenter

import com.google.gson.JsonObject
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.usecase.RequestParams


class PromoCheckoutListDealsPresenter : BaseDaggerPresenter<PromoCheckoutListContract.View>(), PromoCheckoutListDealsContract.Presenter {

    override fun processCheckDealPromoCode(voucherId: String?, requestBody: JsonObject?, flag: Boolean) {
        view.showProgressLoading()
        requestBody!!.addProperty("promocode", voucherId)
        val requestParams = RequestParams.create()
        requestParams.putObject("checkoutdata", requestBody)
        requestParams.putBoolean("ispromocodecase", flag)

    }
}