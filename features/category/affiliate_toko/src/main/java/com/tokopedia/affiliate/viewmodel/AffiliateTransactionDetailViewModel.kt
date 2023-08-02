package com.tokopedia.affiliate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.PAGE_ZERO
import com.tokopedia.affiliate.TRAFFIC_TYPE
import com.tokopedia.affiliate.TYPE_DIVIDER
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.model.response.AffiliateCommissionDetailsData
import com.tokopedia.affiliate.model.response.AffiliateTrafficCommissionCardDetails
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateCommisionDividerItemModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateCommisionThickDividerItemModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateCommissionItemModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateTrafficCardModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateWithdrawalTitleItemModel
import com.tokopedia.affiliate.usecase.AffiliateCommissionDetailsUseCase
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.universal_sharing.usecase.ExtractBranchLinkUseCase
import javax.inject.Inject

class AffiliateTransactionDetailViewModel @Inject constructor(
    private val affiliateCommissionDetailUserCase: AffiliateCommissionDetailsUseCase,
    private val extractBranchLinkUseCase: ExtractBranchLinkUseCase
) : BaseViewModel() {
    private var commssionData = MutableLiveData<AffiliateCommissionDetailsData.GetAffiliateCommissionDetail>()
    private var detailList = MutableLiveData<ArrayList<Visitable<AffiliateAdapterTypeFactory>>>()
    private var detailTitle = MutableLiveData<String>()
    private var errorMessage = MutableLiveData<Throwable>()
    private var progressBar = MutableLiveData<Boolean>()
    private var lastItem: String = "0"
    private var additionKey: String = ""
    private var shimmerVisibility = MutableLiveData<Boolean>()
    private var pageType: String = ""
    var commissionType: String? = ""
    private var applink = MutableLiveData<String>()

    fun affiliateCommission(transactionID: String, page: Int = PAGE_ZERO) {
        launchCatchError(block = {
            if (page == PAGE_ZERO) {
                lastItem = "0"
                progressBar.value = true
                affiliateCommissionDetailUserCase.affiliateCommissionDetails(transactionID).getAffiliateCommissionDetail?.let { affiliateCommissionDetail ->
                    var tempCardDetails: List<AffiliateTrafficCommissionCardDetails.GetAffiliateTrafficCommissionDetailCards.Data.TrafficCommissionCardDetail?>? = null
                    affiliateCommissionDetail.data?.additionQueryKey?.let { key ->
                        additionKey = key
                    }
                    affiliateCommissionDetail.data?.pageType?.let { type ->
                        pageType = type
                    }
                    affiliateCommissionDetail.data?.detailTitle?.let { detailTitle.value = it }
                    commissionType = affiliateCommissionDetail.data?.commissionType
                    if (affiliateCommissionDetail.data?.commissionType == TRAFFIC_TYPE) {
                        affiliateCommissionDetailUserCase.affiliateTrafficCardDetails(additionKey, lastItem, pageType)?.let {
                            tempCardDetails = it.getAffiliateTrafficCommissionDetailCards?.data?.trafficCommissionCardDetail
                            it.getAffiliateTrafficCommissionDetailCards?.data?.lastID?.let { lastID ->
                                lastItem = lastID
                            }
                        }
                    }
                    detailList.value = getDetailListOrganize(affiliateCommissionDetail.data?.detail, tempCardDetails)
                    commssionData.value = affiliateCommissionDetail
                }
                progressBar.value = false
            } else {
                shimmerVisibility.value = true
                affiliateCommissionDetailUserCase.affiliateTrafficCardDetails(additionKey, lastItem, pageType)?.let {
                    shimmerVisibility.value = false
                    it.getAffiliateTrafficCommissionDetailCards?.data?.lastID?.let { lastID ->
                        lastItem = lastID
                    }
                    detailList.value = getDetailListOrganize(null, it.getAffiliateTrafficCommissionDetailCards?.data?.trafficCommissionCardDetail, page)
                }
            }
        }, onError = {
                if (page != PAGE_ZERO) {
                    shimmerVisibility.value = false
                } else {
                    progressBar.value = false
                }
                it.printStackTrace()
                errorMessage.value = it
            })
    }

    fun getDetailListOrganize(
        detail: List<AffiliateCommissionDetailsData.GetAffiliateCommissionDetail.Data.Detail?>?,
        trafficCommissionCardDetail: List<AffiliateTrafficCommissionCardDetails.GetAffiliateTrafficCommissionDetailCards.Data.TrafficCommissionCardDetail?>? = null,
        page: Int? = PAGE_ZERO
    ): ArrayList<Visitable<AffiliateAdapterTypeFactory>> {
        val tempList = ArrayList<Visitable<AffiliateAdapterTypeFactory>>()
        detail?.forEach {
            if (it?.detailType != TYPE_DIVIDER) {
                tempList.add(AffiliateCommissionItemModel(it))
            } else if (it.detailType == TYPE_DIVIDER) {
                tempList.add(AffiliateCommisionDividerItemModel())
            }
        }
        trafficCommissionCardDetail?.let {
            if (page == PAGE_ZERO) {
                tempList.add(AffiliateCommisionThickDividerItemModel())
                tempList.add(AffiliateWithdrawalTitleItemModel("10", pageType))
            }
            it.forEach { cardDetail ->
                tempList.add(AffiliateTrafficCardModel(cardDetail))
            }
        }
        return tempList
    }

    fun extractBranchLink(branchLink: String) {
        launchCatchError(
            block = {
                var deeplink = extractBranchLinkUseCase.invoke(branchLink).android_deeplink
                if (!(deeplink.startsWith(ApplinkConst.APPLINK_CUSTOMER_SCHEME + "://"))) {
                    deeplink = ApplinkConst.APPLINK_CUSTOMER_SCHEME + "://" + deeplink
                }
                applink.value = deeplink
            },
            onError = { errorMessage.value = it }
        )
    }

    fun getErrorMessage(): LiveData<Throwable> = errorMessage
    fun getCommissionData(): LiveData<AffiliateCommissionDetailsData.GetAffiliateCommissionDetail> = commssionData
    fun getDetailList(): LiveData<ArrayList<Visitable<AffiliateAdapterTypeFactory>>> = detailList
    fun getDetailTitle(): LiveData<String> = detailTitle
    fun progressBar(): LiveData<Boolean> = progressBar
    fun getShimmerVisibility(): LiveData<Boolean> = shimmerVisibility
    fun getAppLink(): LiveData<String> = applink
}
