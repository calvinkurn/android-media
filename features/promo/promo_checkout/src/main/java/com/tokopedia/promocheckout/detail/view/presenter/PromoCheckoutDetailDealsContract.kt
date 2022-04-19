package com.tokopedia.promocheckout.detail.view.presenter

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

interface PromoCheckoutDetailDealsContract {
    interface Presenter : CustomerPresenter<PromoCheckoutDetailContract.View> {
        fun getDetailPromo(slug: String)
        fun processCheckDealPromoCode(code: List<String>, categoryName: String, metaData: String, grandTotal: Int)
    }
}