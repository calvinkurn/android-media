package com.tokopedia.affiliate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.affiliate.model.response.AffiliateCommissionDetailsData
import com.tokopedia.affiliate.usecase.AffiliateCommissionDetailsUseCase
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import javax.inject.Inject

class AffiliateTransactionDetailViewModel  @Inject constructor(
    private val affiliateCommissionDetailUserCase: AffiliateCommissionDetailsUseCase
) : BaseViewModel() {
    private var commssionData = MutableLiveData<AffiliateCommissionDetailsData.GetAffiliateCommissionDetail>()
    private var errorMessage = MutableLiveData<Throwable>()

    fun affiliateCommission(transactionID:String) {
        launchCatchError(block = {
           commssionData.value = affiliateCommissionDetailUserCase.affiliateCommissionDetails(transactionID).getAffiliateCommissionDetail
        }, onError = {
            it.printStackTrace()
            errorMessage.value = it
        })
    }
    fun getErrorMessage(): LiveData<Throwable> = errorMessage
    fun getCommissionData() : LiveData<AffiliateCommissionDetailsData.GetAffiliateCommissionDetail> = commssionData
}