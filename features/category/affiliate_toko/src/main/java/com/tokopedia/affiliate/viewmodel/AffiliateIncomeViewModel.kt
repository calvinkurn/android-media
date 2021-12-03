package com.tokopedia.affiliate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.model.pojo.AffiliateDatePickerData
import com.tokopedia.affiliate.model.response.AffiliateBalance
import com.tokopedia.affiliate.model.response.AffiliateTransactionHistoryData
import com.tokopedia.affiliate.repository.AffiliateRepository
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateBottomDatePicker
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateTransactionHistoryItemModel
import com.tokopedia.affiliate.usecase.AffiliateBalanceDataUseCase
import com.tokopedia.affiliate.usecase.AffiliateTransactionHistoryUseCase
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.user.session.UserSessionInterface

class AffiliateIncomeViewModel : BaseViewModel(){

    private var affiliateBalanceData = MutableLiveData<AffiliateBalance.AffiliateBalance.Data>()
    private var shimmerVisibility = MutableLiveData<Boolean>()
    private var errorMessage = MutableLiveData<Throwable>()
    private var affiliateDataList = MutableLiveData<ArrayList<Visitable<AffiliateAdapterTypeFactory>>>()
    private var rangeChanged = MutableLiveData<Boolean>()
    var hasNext = true

    private val affiliateBalanceDataUseCase = AffiliateBalanceDataUseCase(AffiliateRepository())
    private val affiliateTransactionHistoryUseCase = AffiliateTransactionHistoryUseCase(AffiliateRepository())

    fun getAffiliateBalance() {
        launchCatchError(block = {
            affiliateBalanceData.value =
                    affiliateBalanceDataUseCase.getAffiliateBalance().affiliateBalance.data
        }, onError = {
            it.printStackTrace()
        })
    }

    fun getAffiliateTransactionHistory(page: Int) {
        shimmerVisibility.value = true
        launchCatchError(block = {
            affiliateTransactionHistoryUseCase.getAffiliateTransactionHistory(selectedDateValue.toInt() ?: 0, page).getAffiliateTransactionHistory.data.let {
                hasNext = it.hasNext
                convertDataToVisitables(it)?.let { visitables ->
                    affiliateDataList.value = visitables
                }
            }
        }, onError = {
            shimmerVisibility.value = false
            it.printStackTrace()
            errorMessage.value = it
        })
    }

    private fun convertDataToVisitables(data: AffiliateTransactionHistoryData.GetAffiliateTransactionHistory.Data): ArrayList<Visitable<AffiliateAdapterTypeFactory>>? {
        val tempList: ArrayList<Visitable<AffiliateAdapterTypeFactory>> = ArrayList()
        data.transaction?.let { items ->
            for (transaction in items) {
                transaction.let {
                    tempList.add(AffiliateTransactionHistoryItemModel(transaction))
                }
            }
            return tempList
        }
        return null
    }
    private var selectedDateRange = AffiliateBottomDatePicker.SEVEN_DAYS
    private var selectedDateValue = "7"
    fun getSelectedDate(): String {
        return selectedDateRange
    }

    fun onRangeChanged(range: AffiliateDatePickerData) {
        if (selectedDateRange != range.text) {
            selectedDateRange = range.text
            selectedDateValue = range.value
            rangeChanged.value = true
        }
    }
    fun getAffiliateBalanceData(): LiveData<AffiliateBalance.AffiliateBalance.Data> = affiliateBalanceData
    fun getShimmerVisibility(): LiveData<Boolean> = shimmerVisibility
    fun getAffiliateDataItems() : LiveData<ArrayList<Visitable<AffiliateAdapterTypeFactory>>> = affiliateDataList
    fun getErrorMessage(): LiveData<Throwable> = errorMessage
    fun getRangeChange() :LiveData<Boolean> = rangeChanged
}