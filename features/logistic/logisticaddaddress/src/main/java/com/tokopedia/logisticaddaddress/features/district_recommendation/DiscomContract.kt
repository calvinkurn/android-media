package com.tokopedia.logisticaddaddress.features.district_recommendation

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.logisticdata.data.entity.address.Token

interface DiscomContract {

    interface View : BaseListViewListener<AddressViewModel> {

        override fun showGetListError(throwable: Throwable?)

        fun showLoading()

        fun hideLoading()

        fun showNoResultMessage()

        fun showInitialLoadMessage()
    }

    interface Presenter {

        fun attach(view: View)

        fun detach()

        fun loadData(query: String, page: Int)

        fun loadData(query: String, token: Token, page: Int)

    }

    interface Constant {
        companion object {
            const val INTENT_DISTRICT_RECOMMENDATION_ADDRESS = "district_recommendation_address"
            const val ARGUMENT_DATA_TOKEN = "token"
        }
    }
}
