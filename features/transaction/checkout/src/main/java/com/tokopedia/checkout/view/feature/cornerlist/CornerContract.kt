package com.tokopedia.checkout.view.feature.cornerlist

import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel

/**
 * Created by fajarnuha on 2019-05-26.
 */
interface CornerContract {

    interface View {
        fun showEmptyView()
        fun showData(data: List<RecipientAddressModel>)
        fun appendData(data: List<RecipientAddressModel>)
        fun notifyHasNotNextPage()
        fun setLoadingState(active: Boolean)
        fun showError(e: Throwable)
    }

    interface Presenter {
        fun attachView(view: View)
        fun detachView()
        fun getData()
        fun loadMore(page: Int)
        fun searchQuery(query: String)
    }
}