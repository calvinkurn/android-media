package com.tokopedia.affiliate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.TRAFFIC_TYPE
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.model.response.AffiliateCommissionDetailsData
import com.tokopedia.affiliate.model.response.AffiliateTrafficCommissionCardDetails
import com.tokopedia.affiliate.ui.viewholder.viewmodel.*
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
    private var lastItem :String?= "0"
    fun affiliateCommission(transactionID:String) {
        launchCatchError(block = {
            progressBar.value = true
           affiliateCommissionDetailUserCase.affiliateCommissionDetails(transactionID).getAffiliateCommissionDetail?.let {affiliateCommissionDetail ->
               if(affiliateCommissionDetail.data?.commissionType != TRAFFIC_TYPE) detailList.value = getDetailListOrganize(affiliateCommissionDetail?.data?.detail)
               else {
                   affiliateCommissionDetailUserCase.affiliateTrafficCardDetails("transactionDate",lastItem,affiliateCommissionDetail.data?.pageType)?.let {
                       detailList.value = getDetailListOrganize(affiliateCommissionDetail?.data?.detail,it.getAffiliateTrafficCommissionDetailCards?.data?.trafficCommissionCardDetail)
                       lastItem = it?.getAffiliateTrafficCommissionDetailCards?.data?.lastID
                   }
               }
               commssionData.value = affiliateCommissionDetail
           }
            progressBar.value = false
        }, onError = {
            progressBar.value = false
            it.printStackTrace()
            errorMessage.value = it
        })
    }

     fun getDetailListOrganize(
         detail: List<AffiliateCommissionDetailsData.GetAffiliateCommissionDetail.Data.Detail?>?,
         trafficCommissionCardDetail: List<AffiliateTrafficCommissionCardDetails.GetAffiliateTrafficCommissionDetailCards.Data.TrafficCommissionCardDetail?>? = null
     ): ArrayList<Visitable<AffiliateAdapterTypeFactory>> {
            var tempList = ArrayList<Visitable<AffiliateAdapterTypeFactory>>()
            detail?.forEach {
                if(it?.detailType != TYPE_DIVIDER)
                    tempList.add(AffiliateCommissionItemModel(it))
                else if(it.detailType == TYPE_DIVIDER)
                    tempList.add(AffiliateCommisionDividerItemModel())
            }
         tempList.add(AffiliateCommisionThickDividerItemModel())
         tempList.add(AffiliateWithdrawalTitleItemModel("10"))
         trafficCommissionCardDetail?.forEach { cardDetail ->
            tempList.add(AffiliateTrafficCardModel(cardDetail))
         }
        return tempList
    }

    fun getErrorMessage(): LiveData<Throwable> = errorMessage
    fun getCommissionData() : LiveData<AffiliateCommissionDetailsData.GetAffiliateCommissionDetail> = commssionData
    fun getDetailList(): LiveData<ArrayList<Visitable<AffiliateAdapterTypeFactory>>> = detailList
    fun progressBar(): LiveData<Boolean> = progressBar
}