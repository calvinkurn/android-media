package com.tokopedia.logisticaddaddress.features.district_recommendation

import com.tokopedia.logisticCommon.data.entity.response.Data
import com.tokopedia.logisticaddaddress.domain.model.Address

@Deprecated("Please use MVVM with DiscomViewModel")
interface DiscomContract {

    interface View {
        fun renderData(list: List<Address>, hasNextPage: Boolean)
        fun showGetListError(throwable: Throwable)
        fun setLoadingState(active: Boolean)
        fun showEmpty()
        fun setResultDistrict(data: Data, lat: Double, long: Double)
        fun showToasterError(message: String)
    }

    interface Presenter {
        fun attach(view: View)
        fun detach()
        fun loadData(query: String, page: Int)
        fun autoFill(lat: Double, long: Double)
        fun setLocationAvailability(available: Boolean)
        fun getLocationAvailability(): Boolean
    }

    interface Constant {
        companion object {
            const val INTENT_DISTRICT_RECOMMENDATION_ADDRESS = "district_recommendation_address"
            const val ARGUMENT_DATA_TOKEN = "token"
            const val IS_LOCALIZATION = "is_localization"
        }
    }
}
