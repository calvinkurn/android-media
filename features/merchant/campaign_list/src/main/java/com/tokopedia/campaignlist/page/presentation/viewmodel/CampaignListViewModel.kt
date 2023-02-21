package com.tokopedia.campaignlist.page.presentation.viewmodel

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.campaignlist.common.data.model.response.Campaign
import com.tokopedia.campaignlist.common.data.model.response.CampaignDynamicRule
import com.tokopedia.campaignlist.common.data.model.response.CampaignListV2
import com.tokopedia.campaignlist.common.data.model.response.CampaignStatus
import com.tokopedia.campaignlist.common.data.model.response.CampaignTypeData
import com.tokopedia.campaignlist.common.data.model.response.GetCampaignListV2
import com.tokopedia.campaignlist.common.data.model.response.GetCampaignListV2Response
import com.tokopedia.campaignlist.common.data.model.response.GetMerchantCampaignBannerGeneratorData
import com.tokopedia.campaignlist.common.data.model.response.GetMerchantCampaignBannerGeneratorDataResponse
import com.tokopedia.campaignlist.common.data.model.response.GetSellerCampaignSellerAppMeta
import com.tokopedia.campaignlist.common.data.model.response.GetSellerCampaignSellerAppMetaResponse
import com.tokopedia.campaignlist.common.data.model.response.SellerCampaignInfo
import com.tokopedia.campaignlist.common.data.model.response.ShopData
import com.tokopedia.campaignlist.common.usecase.GetCampaignListUseCase
import com.tokopedia.campaignlist.common.usecase.GetCampaignListUseCase.Companion.NPL_CAMPAIGN_TYPE
import com.tokopedia.campaignlist.common.usecase.GetCampaignListUseCase.Companion.NPL_LIST_TYPE
import com.tokopedia.campaignlist.common.usecase.GetMerchantBannerUseCase
import com.tokopedia.campaignlist.common.usecase.GetSellerMetaDataUseCase
import com.tokopedia.campaignlist.common.util.PreferenceDataStore
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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CampaignListViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider,
    private val dispatchers: CoroutineDispatchers,
    private val getCampaignListUseCase: GetCampaignListUseCase,
    private val getMerchantBannerUseCase: GetMerchantBannerUseCase,
    private val getSellerMetaDataUseCase: GetSellerMetaDataUseCase,
    private val preferenceDataStore: PreferenceDataStore
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


    sealed class UiEvent {
        data class TapShareButton(val campaignId: Int): UiEvent()
        data class CampaignStatusFilterApplied(val selectedCampaignStatus: CampaignStatusSelection) : UiEvent()
        data class CampaignTypeFilterApplied(val selectedCampaignType: CampaignTypeSelection) : UiEvent()
        object ClearFilter : UiEvent()
        object NoCampaignStatusFilterApplied : UiEvent()
        object DismissTicker: UiEvent()
    }

    sealed class UiEffect {
        object None : UiEffect()
        data class ShowShareBottomSheet(val banner: GetMerchantCampaignBannerGeneratorData) : UiEffect()
    }

    data class UiState(
        val campaigns: List<ActiveCampaign> = listOf(),
        val campaignStatus: List<CampaignStatusSelection> = emptyList(),
        val campaignType: List<CampaignTypeSelection> = emptyList(),
        val selectedCampaignStatus: CampaignStatusSelection? = null,
        val selectedCampaignType: CampaignTypeSelection? = null,
        val isTickerDismissed: Boolean = false,
        val showClearFilterIcon : Boolean = true
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<UiEffect>(replay = 1)
    val uiEffect = _uiEffect.asSharedFlow()

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

    fun checkTickerState() {
        _uiState.update {
            it.copy(isTickerDismissed = preferenceDataStore.isTickerDismissed())
        }
    }

    fun getCampaignList(
        campaignName: String = "",
        campaignTypeId: Int = NPL_CAMPAIGN_TYPE,
        listTypeId: Int = NPL_LIST_TYPE,
        statusId: List<Int> = GetCampaignListUseCase.statusId
    ) {
        launchCatchError(block = {
            val mockedResponseResult = withContext(dispatchers.io) {
                populateMockedCampaignListResponse(campaignItemNumber = 100)
            }


            getCampaignListResultLiveData.value = Success(mockedResponseResult)

            _uiState.update {
                it.copy(campaigns = mapCampaignListDataToActiveCampaignList(mockedResponseResult.getCampaignListV2.campaignList))
            }
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

            _uiEffect.emit(UiEffect.ShowShareBottomSheet(result.getMerchantCampaignBannerGeneratorData))
        }, onError = {
            getMerchantBannerResultLiveData.value = Fail(it)
        })
    }

    fun getSellerMetaData() {
        launchCatchError(block = {
            val mockedResult = withContext(dispatchers.io) {
                populateMockedCampaignData()
            }
            getSellerMetaDataResultLiveData.value = Success(mockedResult)

            val campaignType = mapCampaignTypeDataToCampaignTypeSelections(mockedResult.getSellerCampaignSellerAppMeta.campaignTypeData)
            val campaignStatus = mapCampaignStatusToCampaignStatusSelections(mockedResult.getSellerCampaignSellerAppMeta.campaignStatus)
            setDefaultCampaignTypeSelection(campaignType)

            _uiState.update {
                it.copy(
                    campaignStatus = campaignStatus,
                    campaignType = campaignType,
                    selectedCampaignType = getSelectedCampaignTypeSelection()
                )
            }
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

    fun onEvent(event: UiEvent) {
        when(event) {
            is UiEvent.CampaignStatusFilterApplied -> {
                _uiState.update { it.copy(selectedCampaignStatus = event.selectedCampaignStatus, showClearFilterIcon = true) }
            }
            is UiEvent.CampaignTypeFilterApplied -> {
                _uiState.update { it.copy(selectedCampaignType = event.selectedCampaignType, showClearFilterIcon = true) }
            }
            UiEvent.NoCampaignStatusFilterApplied -> {
                _uiState.update { it.copy(selectedCampaignStatus = null, showClearFilterIcon = true) }
            }
            UiEvent.DismissTicker -> {
                preferenceDataStore.markTickerAsDismissed()
                _uiState.update { it.copy(isTickerDismissed = true) }
            }
            is UiEvent.TapShareButton -> { getSellerBanner(event.campaignId) }
            UiEvent.ClearFilter -> {
                _uiState.update { it.copy(showClearFilterIcon = false, selectedCampaignType = selectedCampaignTypeSelection, selectedCampaignStatus = null ) }
                getCampaignList()
            }
        }
    }

    private fun populateMockedCampaignListResponse(campaignItemNumber: Int): GetCampaignListV2Response {
        val campaignList = mutableListOf<CampaignListV2>()

        repeat(campaignItemNumber) { index ->
            campaignList.add(
                CampaignListV2(
                    index.toString(),
                    "Campaign $index",
                    "73",
                    "Rilisan Spesial",
                    "5",
                    "Tersedia",
                    "Selesai 31 Dec 22",
                    "31-12-2022 12:37 WIB",
                    "31-12-2022 13:07 WIB",
                    "05-10-2022 10:39",
                    "05-10-2022 10:39",
                    -1202,
                    "05-10-2022 10:40",
                    "31-12-2022 11:17",
                    "Min. Diskon 100%",
                    campaignBanner = emptyList(),
                    campaignDynamicRule = CampaignDynamicRule(),
                    sellerCampaignInfo = SellerCampaignInfo(AcceptedProduct = 10),
                    coverImg = "https://images.tokopedia.net/img/AoawBM/2022/10/5/32c161c2-5210-420c-9cb3-5bf3f3b1c121.jpg"
                )
            )
        }

        return GetCampaignListV2Response(
            getCampaignListV2 = GetCampaignListV2(
                campaignList,
                totalCampaign = campaignItemNumber,
                totalCampaignActive = campaignItemNumber,
                enableRules = true,
                totalCampaignFinished = 0,
                useRestrictionEngine = true
            )
        )
    }

    private fun populateMockedCampaignData() : GetSellerCampaignSellerAppMetaResponse {
        return GetSellerCampaignSellerAppMetaResponse(
            GetSellerCampaignSellerAppMeta(
                campaignStatus = listOf(
                    CampaignStatus(listOf(7), "Berlangsung"),
                    CampaignStatus(listOf(5), "Tersedia"),
                    CampaignStatus(listOf(6, 14), "Mendatang")
                ),
                campaignTypeData = listOf(
                    CampaignTypeData("73", "Rilisan Spesial", 1, ""),
                    CampaignTypeData("162", "Flash Sale Toko", 1, "Segera Hadir")
                )
            )
        )
    }
}
