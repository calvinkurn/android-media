package com.tokopedia.logisticaddaddress.features.district_recommendation

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.logisticaddaddress.domain.model.Address
import com.tokopedia.logisticdata.data.entity.address.Token

interface DiscomContract {

    interface View {
        fun renderData(list: List<Address>, hasNextPage: Boolean)
        fun showGetListError(throwable: Throwable)
        fun setLoadingState(active: Boolean)
        fun showEmpty()
    }

    interface Presenter {
        fun attach(view: View)
        fun detach()
        fun loadData(query: String, page: Int)
        fun loadData(query: String, page: Int, token: Token)

    }

    interface Constant {
        companion object {
            const val INTENT_DISTRICT_RECOMMENDATION_ADDRESS = "district_recommendation_address"
            const val ARGUMENT_DATA_TOKEN = "token"
        }
    }
}
