package com.tokopedia.logisticaddaddress.features.district_recommendation

import com.tokopedia.logisticaddaddress.domain.model.Address
import com.tokopedia.logisticCommon.data.entity.address.Token
import com.tokopedia.logisticCommon.data.entity.response.Data

interface DiscomContract {

    interface View {
        fun renderData(list: List<Address>, hasNextPage: Boolean)
        fun showGetListError(throwable: Throwable)
        fun setLoadingState(active: Boolean)
        fun showEmpty()
        fun setResultDistrict(data: Data, lat: Double, long: Double)
        fun showToasterError()
    }

    interface Presenter {
        fun attach(view: View)
        fun detach()
        fun loadData(query: String, page: Int)
        fun loadData(query: String, page: Int, token: Token)
        fun autoFill(lat: Double, long: Double)
    }

    interface Constant {
        companion object {
            const val INTENT_DISTRICT_RECOMMENDATION_ADDRESS = "district_recommendation_address"
            const val ARGUMENT_DATA_TOKEN = "token"
            const val IS_LOCALIZATION = "is_localization"
        }
    }
}
