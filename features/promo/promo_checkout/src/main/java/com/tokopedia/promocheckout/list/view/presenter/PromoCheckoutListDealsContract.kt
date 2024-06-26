package com.tokopedia.promocheckout.list.view.presenter

import android.content.res.Resources
import com.google.gson.JsonObject
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

interface PromoCheckoutListDealsContract {

    interface Presenter : CustomerPresenter<PromoCheckoutListContract.View> {
        fun processCheckDealPromoCode(code: List<String>, categoryName: String, metaData: String, grandTotal: Int)
        fun getListTravelCollectiveBanner(resources: Resources)
    }
}