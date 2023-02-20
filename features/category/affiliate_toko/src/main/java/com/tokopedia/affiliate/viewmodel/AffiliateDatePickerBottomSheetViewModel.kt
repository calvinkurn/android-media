package com.tokopedia.affiliate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.adapter.bottomSheetsAdapter.AffiliateBottomSheetTypeFactory
import com.tokopedia.affiliate.model.pojo.AffiliateDatePickerData
import com.tokopedia.affiliate.model.response.AffiliateDateFilterResponse
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateBottomDatePicker
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateDateRangePickerModel
import com.tokopedia.affiliate.usecase.AffiliateUserPerformanceUseCase
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import java.util.ArrayList
import javax.inject.Inject

class AffiliateDatePickerBottomSheetViewModel @Inject constructor(
    private val affiliateUserPerformanceUseCase: AffiliateUserPerformanceUseCase
) : BaseViewModel() {
    var rangeSelected = ""
    var identifier = ""
    private var shimmerVisibility = MutableLiveData<Boolean>()
    private var tickerInformation = MutableLiveData<String>()
    private var affiliateFilterList =
        MutableLiveData<ArrayList<Visitable<AffiliateBottomSheetTypeFactory>>>()
    private var error = MutableLiveData<Boolean>()
    private var source = AffiliateBottomDatePicker.IDENTIFIER_WITHDRAWAL
    fun getAffiliateFilterData(source: String = AffiliateBottomDatePicker.IDENTIFIER_WITHDRAWAL) {
        this.source = source
        launchCatchError(block = {
            shimmerVisibility.value = true
            affiliateUserPerformanceUseCase.getAffiliateFilter().let { response ->
                response.data?.ticker?.let { ticker ->
                    if (ticker.isNotEmpty()) tickerInformation.value =
                        ticker.first()?.tickerDescription
                }

                affiliateFilterList.value =
                    convertFilterToVisitable(response.data?.getAffiliateDateFilter)
            }
            shimmerVisibility.value = false
        }, onError = {
                error.value = true
                shimmerVisibility.value = false
                it.printStackTrace()
            })
    }

    fun convertFilterToVisitable(
        affiliateDateFilter: List<AffiliateDateFilterResponse.Data.GetAffiliateDateFilter?>?
    ): ArrayList<Visitable<AffiliateBottomSheetTypeFactory>> {
        val itemList: ArrayList<Visitable<AffiliateBottomSheetTypeFactory>> = ArrayList()
        affiliateDateFilter?.forEach { filter ->
            val title = filter?.filterTitle.orEmpty()
            itemList.add(
                AffiliateDateRangePickerModel(
                    AffiliateDatePickerData(
                        title,
                        rangeSelected == title,
                        filter?.filterValue.orEmpty(),
                        filter?.filterDescription.orEmpty(),
                        identifier == AffiliateBottomDatePicker.IDENTIFIER_HOME,
                        filter?.updateDescription.orEmpty()
                    ),
                    source
                )
            )
        }
        return itemList
    }

    fun updateItem(position: Int) {
        affiliateFilterList.value?.forEachIndexed { index, visitable ->
            (visitable as AffiliateDateRangePickerModel).dateRange.isSelected = index == position
        }
    }

    fun getItemList(): ArrayList<Visitable<AffiliateBottomSheetTypeFactory>>? {
        return affiliateFilterList.value
    }

    fun getAffiliateFilterItems(): LiveData<ArrayList<Visitable<AffiliateBottomSheetTypeFactory>>> =
        affiliateFilterList

    fun getShimmerVisibility(): LiveData<Boolean> = shimmerVisibility
    fun getTickerInfo(): LiveData<String> = tickerInformation
    fun getError(): LiveData<Boolean> = error
}
