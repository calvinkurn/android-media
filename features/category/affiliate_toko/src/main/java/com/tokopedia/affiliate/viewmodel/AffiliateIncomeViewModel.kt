package com.tokopedia.affiliate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.PROJECT_ID
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.model.pojo.AffiliateDatePickerData
import com.tokopedia.affiliate.model.response.AffiliateBalance
import com.tokopedia.affiliate.model.response.AffiliateKycDetailsData
import com.tokopedia.affiliate.model.response.AffiliateTransactionHistoryData
import com.tokopedia.affiliate.repository.AffiliateRepository
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateBottomDatePicker
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateTransactionHistoryItemModel
import com.tokopedia.affiliate.usecase.AffiliateBalanceDataUseCase
import com.tokopedia.affiliate.usecase.AffiliateKycUseCase
import com.tokopedia.affiliate.usecase.AffiliateTransactionHistoryUseCase
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError

class AffiliateIncomeViewModel : BaseViewModel(){

    private var affiliateBalanceData = MutableLiveData<AffiliateBalance.AffiliateBalance.Data>()
    private var affiliateKycData = MutableLiveData<AffiliateKycDetailsData.KycProjectInfo>()
    private var affiliateKycError = MutableLiveData<String>()
    private var affiliateKycLoader = MutableLiveData<Boolean>()
    private var shimmerVisibility = MutableLiveData<Boolean>()
    private var errorMessage = MutableLiveData<Throwable>()
    private var affiliateDataList = MutableLiveData<ArrayList<Visitable<AffiliateAdapterTypeFactory>>>()
    private var rangeChanged = MutableLiveData<Boolean>()
    var hasNext = true

     val affiliateBalanceDataUseCase = AffiliateBalanceDataUseCase(AffiliateRepository())
     val affiliateTransactionHistoryUseCase = AffiliateTransactionHistoryUseCase(AffiliateRepository())
     val affiliatKycHistoryUseCase = AffiliateKycUseCase(AffiliateRepository())


    fun getAffiliateBalance() {
        launchCatchError(block = {
                    affiliateBalanceDataUseCase.getAffiliateBalance().affiliateBalance?.data?.let {
                        affiliateBalanceData.value = it
                    }
        }, onError = {
            it.printStackTrace()
        })
    }

    fun getKycDetails() {
        launchCatchError(block = {
            affiliateKycLoader.value = true
            affiliatKycHistoryUseCase.getKycInformation(PROJECT_ID).kycProjectInfo?.let {
                affiliateKycData.value = it
            }
            affiliateKycLoader.value = false
        }, onError = {
            affiliateKycError.value = it.message
            affiliateKycLoader.value = false
            it.printStackTrace()
        })
    }

    fun getAffiliateTransactionHistory(page: Int) {
        shimmerVisibility.value = true
        launchCatchError(block = {
            affiliateTransactionHistoryUseCase.getAffiliateTransactionHistory(selectedDateValue.toInt() ?: 0, page).getAffiliateTransactionHistory?.data?.let {
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

     fun convertDataToVisitables(data: AffiliateTransactionHistoryData.GetAffiliateTransactionHistory.Data?): ArrayList<Visitable<AffiliateAdapterTypeFactory>>? {
        val tempList: ArrayList<Visitable<AffiliateAdapterTypeFactory>> = ArrayList()
        data?.transaction?.let { items ->
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
    fun getAffiliateKycData(): LiveData<AffiliateKycDetailsData.KycProjectInfo> = affiliateKycData
    fun getAffiliateKycLoader(): LiveData<Boolean> = affiliateKycLoader
    fun getAffiliateDataItems() : LiveData<ArrayList<Visitable<AffiliateAdapterTypeFactory>>> = affiliateDataList
    fun getErrorMessage(): LiveData<Throwable> = errorMessage
    fun getKycErrorMessage(): LiveData<String> = affiliateKycError
    fun getRangeChange() :LiveData<Boolean> = rangeChanged
}