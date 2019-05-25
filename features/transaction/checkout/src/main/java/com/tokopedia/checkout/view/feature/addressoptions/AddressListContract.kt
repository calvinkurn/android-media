package com.tokopedia.checkout.view.feature.addressoptions

import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel

/**
 * Created by fajarnuha on 2019-05-24.
 */
interface AddressListContract {

    interface Presenter {
        fun attachView(view: ISearchAddressListView<List<RecipientAddressModel>>)
        fun detachView()
        fun getAddress()
        fun searchAddress(query: String)
        fun loadMore()
    }

}