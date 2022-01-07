package com.tokopedia.campaignlist.page.presentation.viewmodel

import android.text.TextUtils
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
import com.tokopedia.campaignlist.common.util.ResourceProvider
import com.tokopedia.campaignlist.page.presentation.model.ActiveCampaign
import com.tokopedia.campaignlist.page.presentation.model.CampaignStatusSelection
import com.tokopedia.campaignlist.page.presentation.model.CampaignTypeSelection
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerShareData
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CampaignListViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider,
    private val dispatchers: CoroutineDispatchers,
    private val getCampaignListUseCase: GetCampaignListUseCase,
    private val getMerchantBannerUseCase: GetMerchantBannerUseCase,
    private val getSellerMetaDataUseCase: GetSellerMetaDataUseCase
) : BaseViewModel(dispatchers.main) {

    companion object {
        const val NPL_ICON_URL = "https://images.tokopedia.net/img/android/campaign_list/npl_icon.png"
        private const val ONGOING_STATUS_ID = "7"
        private const val NO_OVERLOAD_PRODUCT = 0
        private const val NO_PRODUCT = 0
        private const val DISPLAYED_PRODUCT_COUNT = 3
    }

    private var campaignName = ""
    private var campaignTypeId = NPL_CAMPAIGN_TYPE
    private var campaignStatusId = GetCampaignListUseCase.statusId
    private var selectedActiveCampaign : ActiveCampaign? = null
    private var selectedCampaignTypeSelection: CampaignTypeSelection? = null
    private var merchantBannerData: GetMerchantCampaignBannerGeneratorData? = null

    private val getCampaignListResultLiveData = MutableLiveData<Result<GetCampaignListV2Response>>()
    val getCampaignListResult: LiveData<Result<GetCampaignListV2Response>> get() = getCampaignListResultLiveData

    private val getMerchantBannerResultLiveData = MutableLiveData<Result<GetMerchantCampaignBannerGeneratorDataResponse>>()
    val getMerchantBannerResult: LiveData<Result<GetMerchantCampaignBannerGeneratorDataResponse>> get() = getMerchantBannerResultLiveData

    private val getSellerMetaDataResultLiveData = MutableLiveData<Result<GetSellerCampaignSellerAppMetaResponse>>()
    val getSellerMetaDataResult: LiveData<Result<GetSellerCampaignSellerAppMetaResponse>> get() = getSellerMetaDataResultLiveData

    fun setCampaignName(campaignName : String) {
        this.campaignName = campaignName
    }

    fun getCampaignName(): String {
        return campaignName
    }

    fun setCampaignTypeId(campaignTypeId : Int) {
        this.campaignTypeId = campaignTypeId
    }

    fun getCampaignTypeId(): Int {
        return campaignTypeId
    }

    fun setCampaignStatusId(campaignStatusId : List<Int>) {
        this.campaignStatusId = campaignStatusId
    }

    fun getCampaignStatusId(): List<Int> {
        return campaignStatusId
    }

    fun setSelectedActiveCampaign(activeCampaign: ActiveCampaign) {
        this.selectedActiveCampaign = activeCampaign
    }

    fun getSelectedActiveCampaign(): ActiveCampaign? {
        return selectedActiveCampaign
    }

    fun setDefaultCampaignTypeSelection(campaignTypeSelections: List<CampaignTypeSelection>) {
        selectedCampaignTypeSelection = campaignTypeSelections.find { campaignTypeSelection ->
            campaignTypeSelection.isSelected
        }
    }

    fun getSelectedCampaignTypeSelection(): CampaignTypeSelection? {
        return selectedCampaignTypeSelection
    }

    fun setMerchantBannerData(merchantBannerData: GetMerchantCampaignBannerGeneratorData) {
        this.merchantBannerData = merchantBannerData
    }

    fun getMerchantBannerData(): GetMerchantCampaignBannerGeneratorData? {
        return merchantBannerData
    }

    fun getCampaignList(campaignName: String = "",
                        campaignTypeId: Int = NPL_CAMPAIGN_TYPE,
                        listTypeId: Int = NPL_LIST_TYPE,
                        statusId: List<Int> = GetCampaignListUseCase.statusId) {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                val params = GetCampaignListUseCase.createParams(campaignName, campaignTypeId, listTypeId, statusId)
                getCampaignListUseCase.setRequestParams(params = params.parameters)
                getCampaignListUseCase.executeOnBackground()
            }
            getCampaignListResultLiveData.value = Success(result)
        }, onError = {
            getCampaignListResultLiveData.value = Fail(it)
        })
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
                campaignTypeId = campaignListV2.campaignTypeId,
                campaignStatus = campaignListV2.statusText,
                campaignStatusId = campaignListV2.statusId,
                campaignPictureUrl = campaignListV2.coverImg,
                campaignName = campaignListV2.campaignName,
                productQty = campaignListV2.sellerCampaignInfo.AcceptedProduct.toString(),
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

    fun getShareBottomSheetTitle(shopName: String): String {
        return String.format(resourceProvider.getShareTitle() ?: "", shopName)
    }

    fun getShareDescriptionWording(
            shopData: ShopData,
            campaignData: Campaign,
            merchantBannerData: GetMerchantCampaignBannerGeneratorData,
            shareUri: String?,
            campaignStatusId: String
    ): String {
        return when (campaignStatusId) {
            ONGOING_STATUS_ID -> {
                val template = resourceProvider.getShareOngoingCampaignDescriptionWording() ?: ""
                val endDate = merchantBannerData.formattedEndDate.split(", ").first()
                val endTime = merchantBannerData.formattedEndDate.split(", ").last()
                String.format(template, campaignData.name, shopData.name, endDate, endTime).plus(" $shareUri")
            }
            else -> {
                val template = resourceProvider.getShareCampaignDescriptionWording() ?: ""
                val startDate = merchantBannerData.formattedStartDate.split(", ").first()
                val startTime = merchantBannerData.formattedStartDate.split(", ").last()
                String.format(template, campaignData.name, shopData.name, startDate, startTime).plus(" $shareUri")
            }
        }
    }

    fun generateLinkerShareData(
            shopData: ShopData,
            campaignData: Campaign,
            shareModel: ShareModel,
            campaignStatusId: String
    ): LinkerShareData {
        val linkerData = LinkerData()
        linkerData.apply {
            feature = shareModel.feature
            channel = shareModel.channel
            campaign = shareModel.campaign
            id = shopData.shopId
            linkerData.type = LinkerData.SHOP_TYPE
            name = getShareOgTitle(campaignData.name, shopData.name)
            uri = "https://www.tokopedia.com/${shopData.domain}"
            ogTitle = getShareOgTitle(campaignData.name, shopData.name)
            ogDescription = getShareDescription(shopData.name, campaignStatusId)
            if (!TextUtils.isEmpty(shareModel.ogImgUrl)) {
                ogImageUrl = shareModel.ogImgUrl
            }
            isThrowOnError = true
        }
        val linkerShareData = LinkerShareData()
        linkerShareData.linkerData = linkerData
        return linkerShareData
    }

    private fun getShareOgTitle(campaignName: String, shopName: String): String {
        val template = resourceProvider.getShareOgTitle() ?: ""
        return String.format(template, campaignName, shopName)
    }

    private fun getShareDescription(shopName: String, campaignStatusId: String): String {
        var template = ""
        when (campaignStatusId) {
            ONGOING_STATUS_ID -> {
                template = resourceProvider.getShareOngoingDescription() ?: ""
            }
            else -> {
                template = resourceProvider.getShareDescription() ?: ""
            }
        }
        return String.format(template, shopName)
    }

    fun calculateOverloadProductCount(totalProductCount: Int): Int {
        val overloadProductCount = totalProductCount - DISPLAYED_PRODUCT_COUNT
        return if (overloadProductCount <= NO_OVERLOAD_PRODUCT) {
            NO_OVERLOAD_PRODUCT
        } else {
            overloadProductCount
        }
    }

    fun getProductCount(unsafeProductCount: String): Int {
        return try {
            unsafeProductCount.toInt()
        } catch (e: Exception) {
            NO_PRODUCT
        }
    }
}