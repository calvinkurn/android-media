package com.tokopedia.affiliate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.model.response.AffiliateBalance
import com.tokopedia.affiliate.model.response.AffiliateTransactionHistoryData
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateTransactionHistoryItemModel
import com.tokopedia.affiliate.usecase.AffiliateBalanceDataUseCase
import com.tokopedia.affiliate.usecase.AffiliateTransactionHistoryUseCase
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class AffiliateIncomeViewModel @Inject constructor(
        private val userSessionInterface: UserSessionInterface,
        private val affiliateBalanceDataUseCase: AffiliateBalanceDataUseCase,
        private val affiliateTransactionHistoryUseCase: AffiliateTransactionHistoryUseCase
) : BaseViewModel(){

    private var affiliateBalanceData = MutableLiveData<AffiliateBalance.AffiliateBalance.Data>()
    private var shimmerVisibility = MutableLiveData<Boolean>()
    private var errorMessage = MutableLiveData<String>()
    private var affiliateDataList = MutableLiveData<ArrayList<Visitable<AffiliateAdapterTypeFactory>>>()
    var hasNext = true

    fun getAffiliateBalance() {
        launchCatchError(block = {
            affiliateBalanceData.value =
                    affiliateBalanceDataUseCase.getAffiliateBalance().affiliateBalance.data
        }, onError = {
            it.printStackTrace()
            errorMessage.value = it.toString()
        })
    }

    fun getAffiliateTransactionHistory(startData: String, endData:String, page: Int) {
        shimmerVisibility.value = true
        launchCatchError(block = {
            affiliateTransactionHistoryUseCase.getAffiliateTransactionHistory(startData, endData, page).getAffiliateTransactionHistory.data.let {
                hasNext = it.hasNext
                convertDataToVisitables(it)?.let { visitables ->
                    affiliateDataList.value = visitables
                }
            }
        }, onError = {
            shimmerVisibility.value = false
            it.printStackTrace()
            errorMessage.value = it.toString()
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

    fun getAffiliateBalanceData(): LiveData<AffiliateBalance.AffiliateBalance.Data> = affiliateBalanceData
    fun getShimmerVisibility(): LiveData<Boolean> = shimmerVisibility
    fun getAffiliateDataItems() : LiveData<ArrayList<Visitable<AffiliateAdapterTypeFactory>>> = affiliateDataList
    fun getErrorMessage(): LiveData<String> = errorMessage

    fun getUserName(): String {
        return userSessionInterface.name
    }

    fun getUserProfilePicture(): String {
        return userSessionInterface.profilePicture
    }
}