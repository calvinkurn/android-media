package com.tokopedia.campaignlist.page.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.campaignlist.common.data.model.response.*
import com.tokopedia.campaignlist.common.usecase.GetCampaignListUseCase
import com.tokopedia.campaignlist.common.usecase.GetCampaignListUseCase.Companion.NPL_CAMPAIGN_TYPE
import com.tokopedia.campaignlist.common.usecase.GetCampaignListUseCase.Companion.NPL_LIST_TYPE
import com.tokopedia.campaignlist.common.usecase.GetMerchantBannerUseCase
import com.tokopedia.campaignlist.common.usecase.GetSellerMetaDataUseCase
import com.tokopedia.campaignlist.page.presentation.model.ActiveCampaign
import com.tokopedia.campaignlist.page.presentation.model.CampaignStatusSelection
import com.tokopedia.campaignlist.page.presentation.model.CampaignTypeSelection
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.withContext
import java.net.UnknownHostException
import javax.inject.Inject

class CampaignListViewModel @Inject constructor(
        private val dispatchers: CoroutineDispatchers,
        private val getCampaignListUseCase: GetCampaignListUseCase,
        private val getMerchantBannerUseCase: GetMerchantBannerUseCase,
        private val getSellerMetaDataUseCase: GetSellerMetaDataUseCase
) : BaseViewModel(dispatchers.main) {

    private val getCampaignListResultLiveData = MutableLiveData<Result<GetCampaignListV2Response>>()
    val getCampaignListResult: LiveData<Result<GetCampaignListV2Response>> get() = getCampaignListResultLiveData

    private val getMerchantBannerResultLiveData = MutableLiveData<Result<GetMerchantCampaignBannerGeneratorDataResponse>>()
    val getMerchantBannerResult: LiveData<Result<GetMerchantCampaignBannerGeneratorDataResponse>> get() = getMerchantBannerResultLiveData

    private val getSellerMetaDataResultLiveData = MutableLiveData<Result<GetSellerCampaignSellerAppMetaResponse>>()
    val getSellerMetaDataResult: LiveData<Result<GetSellerCampaignSellerAppMetaResponse>> get() = getSellerMetaDataResultLiveData

    private var campaignName = ""
    private var campaignTypeId = NPL_CAMPAIGN_TYPE
    private var campaignStatusId = GetCampaignListUseCase.statusId

    fun getCampaignList() {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                val params = GetCampaignListUseCase.createParams(campaignName, campaignTypeId, NPL_LIST_TYPE, campaignStatusId)
                getCampaignListUseCase.setRequestParams(params = params.parameters)
                getCampaignListUseCase.executeOnBackground()
            }
            getCampaignListResultLiveData.value = Success(result)
        }, onError = {
            getCampaignListResultLiveData.value = Fail(it)
        })
    }

    fun setSelectedCampaignName(campaignName: String) {
        this.campaignName = campaignName
    }

    fun setSelectedCampaignTypeId(campaignTypeId: Int) {
        this.campaignTypeId = campaignTypeId
    }

    fun setSelectedCampaignStatusId(campaignStatusId: List<Int>) {
        this.campaignStatusId = campaignStatusId
    }

    fun getSellerBanner(campaignId: Int) {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                val params = GetMerchantBannerUseCase.createParams(campaignId)
                getMerchantBannerUseCase.setRequestParams(params = params.parameters)
                getMerchantBannerUseCase.executeOnBackground()
            }
            getMerchantBannerResultLiveData.value = Success(result)
        }, onError = {
            getMerchantBannerResultLiveData.value = Fail(it)
        })
    }

    fun getSellerMetaData() {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                getSellerMetaDataUseCase.executeOnBackground()
            }
            getSellerMetaDataResultLiveData.value = Success(result)
        }, onError = {
            getSellerMetaDataResultLiveData.value = Fail(it)
        })
    }

    fun mapCampaignListDataToActiveCampaignList(data: List<CampaignListV2>): List<ActiveCampaign> {
        return data.map { campaignListV2 ->
            val startDate = campaignListV2.startDate.split(" ").first()
            val startTime = campaignListV2.startDate.split(" ").last()
            val endDate = campaignListV2.endDate.split(" ").first()
            val endTime = campaignListV2.endDate.split(" ").last()
            ActiveCampaign(
                    campaignId = campaignListV2.campaignId,
                    campaignType = campaignListV2.campaignTypeName,
                    campaignStatus = campaignListV2.statusText,
                    campaignStatusId = campaignListV2.statusId,
                    campaignPictureUrl = campaignListV2.coverImg,
                    campaignName = campaignListV2.campaignName,
                    productQty = campaignListV2.sellerCampaignInfo.TotalItem.toString(),
                    startDate = startDate,
                    endDate = endDate,
                    startTime = startTime,
                    endTime = endTime
            )
        }
    }

    fun mapCampaignTypeDataToCampaignTypeSelections(campaignTypeDataList: List<CampaignTypeData>): List<CampaignTypeSelection> {
        return campaignTypeDataList.map { campaignTypeData ->
            val isSelected = campaignTypeData.campaignTypeId == NPL_CAMPAIGN_TYPE.toString()
            CampaignTypeSelection(
                    campaignTypeId = campaignTypeData.campaignTypeId,
                    campaignTypeName = campaignTypeData.campaignTypeName,
                    statusText = campaignTypeData.statusText,
                    isSelected = isSelected
            )
        }
    }

    fun mapCampaignStatusToCampaignStatusSelections(campaignStatusList: List<CampaignStatus>): List<CampaignStatusSelection> {
        return campaignStatusList.map { campaignStatus ->
            CampaignStatusSelection(
                    statusId = campaignStatus.status,
                    statusText = campaignStatus.statusText,
                    isSelected = false
            )
        }
    }
}