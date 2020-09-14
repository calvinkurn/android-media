package com.tokopedia.manageaddress.ui.addresschoice

import com.tokopedia.logisticdata.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticdata.data.entity.address.Token

/**
 * Created by fajarnuha on 2019-05-24.
 */
interface AddressListContract {

    interface View {

        fun showList(list: MutableList<RecipientAddressModel>)

//        fun onChooseCorner(cornerAddressModel: RecipientAddressModel)

        fun updateList(list: MutableList<RecipientAddressModel>)

        fun showListEmpty()

        fun showError(e: Throwable)

        fun showLoading()

        fun hideLoading()

        fun resetPagination()

        fun setToken(token: Token?)

        fun navigateToCheckoutPage(recipientAddressModel: RecipientAddressModel)

        fun stopTrace()

    }

    interface Presenter {
        fun attachView(view: View)
        fun detachView()
        fun getAddress()
        fun searchAddress(query: String)
        fun loadMore()
        fun saveLastCorner(model: RecipientAddressModel)
        fun getLastCorner(): RecipientAddressModel?
    }

}