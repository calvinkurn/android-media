package com.tokopedia.promocheckout.list.view.presenter

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.promocheckout.common.domain.deals.Type

interface DealsCheckoutListContract {

    interface Presenter : CustomerPresenter<PromoCheckoutListContract.View> {
        fun getType(product: Type)
    }
}