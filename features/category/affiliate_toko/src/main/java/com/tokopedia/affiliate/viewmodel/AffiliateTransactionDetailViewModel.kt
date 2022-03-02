package com.tokopedia.affiliate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.TRAFFIC_TYPE
import com.tokopedia.affiliate.TYPE_DIVIDER
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
    private var lastItem :String?= "0"
    private var additionKey: String? = ""
    fun affiliateCommission(transactionID:String) {
        launchCatchError(block = {
            progressBar.value = true
            affiliateCommissionDetailUserCase.affiliateCommissionDetails(transactionID).getAffiliateCommissionDetail?.let {affiliateCommissionDetail ->
               var tempCardDetails: List<AffiliateTrafficCommissionCardDetails.GetAffiliateTrafficCommissionDetailCards.Data.TrafficCommissionCardDetail?>? = null
               additionKey = affiliateCommissionDetail.data?.additionQueryKey
               if(affiliateCommissionDetail.data?.commissionType == TRAFFIC_TYPE) {
                   affiliateCommissionDetailUserCase.affiliateTrafficCardDetails(additionKey, lastItem, affiliateCommissionDetail.data?.pageType)?.let {
                       tempCardDetails = it.getAffiliateTrafficCommissionDetailCards?.data?.trafficCommissionCardDetail
                       lastItem = it.getAffiliateTrafficCommissionDetailCards?.data?.lastID
                   }
               }
               detailList.value = getDetailListOrganize(affiliateCommissionDetail.data?.detail,tempCardDetails)
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
            val tempList = ArrayList<Visitable<AffiliateAdapterTypeFactory>>()
            detail?.forEach {
                if(it?.detailType != TYPE_DIVIDER)
                    tempList.add(AffiliateCommissionItemModel(it))
                else if(it.detailType == TYPE_DIVIDER)
                    tempList.add(AffiliateCommisionDividerItemModel())
            }
         trafficCommissionCardDetail?.let {
             tempList.add(AffiliateCommisionThickDividerItemModel())
             tempList.add(AffiliateWithdrawalTitleItemModel("10"))
             it.forEach { cardDetail ->
                 tempList.add(AffiliateTrafficCardModel(cardDetail))
             }
         }
        return tempList
    }

    fun getErrorMessage(): LiveData<Throwable> = errorMessage
    fun getCommissionData() : LiveData<AffiliateCommissionDetailsData.GetAffiliateCommissionDetail> = commssionData
    fun getDetailList(): LiveData<ArrayList<Visitable<AffiliateAdapterTypeFactory>>> = detailList
    fun progressBar(): LiveData<Boolean> = progressBar
}