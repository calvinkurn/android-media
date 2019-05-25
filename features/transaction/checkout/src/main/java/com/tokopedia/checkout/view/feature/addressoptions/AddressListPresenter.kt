package com.tokopedia.checkout.view.feature.addressoptions

import com.tokopedia.checkout.domain.usecase.GetAddressCornerUseCase
import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel
import javax.inject.Inject

/**
 * Created by fajarnuha on 2019-05-24.
 */
class AddressListPresenter @Inject constructor(val usecase: GetAddressCornerUseCase): AddressListContract.Presenter {

    private var mView: ISearchAddressListView<List<RecipientAddressModel>>? = null

    override fun attachView(view: ISearchAddressListView<List<RecipientAddressModel>>) {
        mView = view
    }

    override fun detachView() {
        mView = null
    }

    override fun getAddress() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun searchAddress(query: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadMore() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}