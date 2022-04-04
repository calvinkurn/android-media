package com.tokopedia.affiliate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.adapter.dateRangePicker.AffiliateDateRangeTypeFactory
import com.tokopedia.affiliate.model.pojo.AffiliateDatePickerData
import com.tokopedia.affiliate.model.response.AffiliateDateFilterResponse
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateBottomDatePicker
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateDateRangePickerModel
import com.tokopedia.affiliate.usecase.AffiliateUserPerformanceUseCase
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.delay
import java.util.ArrayList
import javax.inject.Inject

class AffiliateDatePickerBottomSheetViewModel@Inject constructor(
    private val affiliateUserPerformanceUseCase: AffiliateUserPerformanceUseCase
) : BaseViewModel() {
    var rangeSelected = ""
    var identifier = ""
    private var shimmerVisibility = MutableLiveData<Boolean>()
    private var tickerInformation = MutableLiveData<String>()
    private var affiliateFilterList = MutableLiveData<ArrayList<Visitable<AffiliateDateRangeTypeFactory>>>()
    private var error = MutableLiveData<Boolean>()

    fun getAffiliateFilterData() {
        launchCatchError(block = {
            shimmerVisibility.value = true
            affiliateUserPerformanceUseCase.getAffiliateFilter()?.let { response ->
                response.data?.ticker?.let { ticker->
                    if(ticker.isNotEmpty()) tickerInformation.value = ticker.first()?.tickerDescription
                }
                convertFilterToVisitable(response.data?.getAffiliateDateFilter)
            }
            shimmerVisibility.value = false
        }, onError = {
            error.value = true
            shimmerVisibility.value = false
            it.printStackTrace()
        })
    }

    private fun convertFilterToVisitable(affiliateDateFilter: List<AffiliateDateFilterResponse.Data.GetAffiliateDateFilter?>?) {
        val itemList: ArrayList<Visitable<AffiliateDateRangeTypeFactory>> = ArrayList()
        affiliateDateFilter?.forEach { filter ->
            var title = ""
            var message = ""
            var value = ""
            filter?.filterTitle?.let { title = it }
            filter?.filterDescription?.let { message = it }
            filter?.filterValue?.let { value = it }
            itemList.add(AffiliateDateRangePickerModel(AffiliateDatePickerData(title,rangeSelected == title, value, message,identifier == AffiliateBottomDatePicker.IDENTIFIER_HOME)))
        }
        affiliateFilterList.value = itemList
    }

    fun updateItem(position: Int) {
        affiliateFilterList.value?.forEachIndexed { index, visitable ->
            (visitable as AffiliateDateRangePickerModel).dateRange.isSelected = index == position
        }
    }
    fun getItemList(): ArrayList<Visitable<AffiliateDateRangeTypeFactory>>? {
        return affiliateFilterList.value
    }

    fun getAffiliateFilterItems(): LiveData<ArrayList<Visitable<AffiliateDateRangeTypeFactory>>> = affiliateFilterList
    fun getShimmerVisibility(): LiveData<Boolean> = shimmerVisibility
    fun getTickerInfo(): LiveData<String> = tickerInformation
    fun getError(): LiveData<Boolean> = error

}