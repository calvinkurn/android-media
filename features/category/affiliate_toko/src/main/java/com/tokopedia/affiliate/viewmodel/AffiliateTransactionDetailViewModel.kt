package com.tokopedia.affiliate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.model.response.AffiliateCommissionDetailsData
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateCommisionDividerItemModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateCommisionThickDividerItemModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateCommissionItemModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateWithdrawalTitleItemModel
import com.tokopedia.affiliate.usecase.AffiliateCommissionDetailsUseCase
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import java.util.ArrayList
import javax.inject.Inject

class AffiliateTransactionDetailViewModel  @Inject constructor(
    private val affiliateCommissionDetailUserCase: AffiliateCommissionDetailsUseCase
) : BaseViewModel() {
    private var commssionData = MutableLiveData<AffiliateCommissionDetailsData.GetAffiliateCommissionDetail>()
    private var detailList = MutableLiveData<ArrayList<Visitable<AffiliateAdapterTypeFactory>>>()
    private var errorMessage = MutableLiveData<Throwable>()
    private var progressBar = MutableLiveData<Boolean>()
    private val TYPE_DIVIDER = "divider"
    fun affiliateCommission(transactionID:String) {
        launchCatchError(block = {
            progressBar.value = true
           affiliateCommissionDetailUserCase.affiliateCommissionDetails(transactionID).getAffiliateCommissionDetail?.let {
               detailList.value = getDetailListOrganize(it?.data?.detail)
               commssionData.value = it
           }
            progressBar.value = false
        }, onError = {
            progressBar.value = false
            it.printStackTrace()
            errorMessage.value = it
        })
    }

     fun getDetailListOrganize(detail: List<AffiliateCommissionDetailsData.GetAffiliateCommissionDetail.Data.Detail?>?): ArrayList<Visitable<AffiliateAdapterTypeFactory>> {
            var tempList = ArrayList<Visitable<AffiliateAdapterTypeFactory>>()
            detail?.forEach {
                if(it?.detailType != TYPE_DIVIDER)
                    tempList.add(AffiliateCommissionItemModel(it))
                else if(it.detailType == TYPE_DIVIDER)
                    tempList.add(AffiliateCommisionDividerItemModel())
            }
         tempList.add(AffiliateCommisionThickDividerItemModel())
         tempList.add(AffiliateWithdrawalTitleItemModel("10"))
        return tempList
    }

    fun getErrorMessage(): LiveData<Throwable> = errorMessage
    fun getCommissionData() : LiveData<AffiliateCommissionDetailsData.GetAffiliateCommissionDetail> = commssionData
    fun getDetailList(): LiveData<ArrayList<Visitable<AffiliateAdapterTypeFactory>>> = detailList
    fun progressBar(): LiveData<Boolean> = progressBar
}