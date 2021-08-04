package com.tokopedia.promocheckout.list.view.presenter

import android.content.res.Resources
import com.google.gson.JsonObject
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

@Deprecated("Not needed anymore.")
interface PromoCheckoutListDealsContract {

    interface Presenter : CustomerPresenter<PromoCheckoutListContract.View> {
        fun processCheckDealPromoCode(flag: Boolean, requestParams: JsonObject)
        fun getListTravelCollectiveBanner(resources: Resources)
    }
}