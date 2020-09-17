package com.tokopedia.promocheckout.list.view.presenter

import com.google.gson.JsonObject
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

interface PromoCheckoutListDealsContract {

    interface Presenter : CustomerPresenter<PromoCheckoutListContract.View> {
        fun getType(product: Type)
    }
}