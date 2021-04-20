package com.tokopedia.promocheckout.detail.view.presenter

import com.google.gson.JsonObject
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

interface PromoCheckoutDetailDealsContract {
    interface Presenter : CustomerPresenter<PromoCheckoutDetailContract.View> {
        fun getDetailPromo(slug: String)
        fun processCheckDealPromoCode(promoCode: String, flag: Boolean, requestBody: JsonObject)
    }
}