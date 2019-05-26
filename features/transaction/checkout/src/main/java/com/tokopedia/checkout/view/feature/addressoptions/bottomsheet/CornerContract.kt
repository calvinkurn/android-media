package com.tokopedia.checkout.view.feature.addressoptions.bottomsheet

import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel

/**
 * Created by fajarnuha on 2019-05-26.
 */
interface CornerContract {

    interface View {
        fun showEmptyView()
        fun setData(data: List<RecipientAddressModel>?)
        fun setLoadingState(active: Boolean)
        fun showError(e: Throwable)
    }

    interface Presenter {
        fun attachView(view: View)
        fun getData()
        fun searchQuery(query: String)
    }
}