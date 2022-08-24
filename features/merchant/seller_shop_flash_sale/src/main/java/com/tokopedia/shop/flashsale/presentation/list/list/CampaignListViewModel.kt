package com.tokopedia.shop.flashsale.presentation.list.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.common.domain.interactor.GqlShopPageGetHomeType
import com.tokopedia.shop.flashsale.common.extension.digitsOnly
import com.tokopedia.shop.flashsale.common.extension.isNumber
import com.tokopedia.shop.flashsale.common.tracker.ShopFlashSaleTracker
import com.tokopedia.shop.flashsale.domain.entity.CampaignMeta
import com.tokopedia.shop.flashsale.domain.entity.CampaignUiModel
import com.tokopedia.shop.flashsale.domain.entity.aggregate.CampaignCreationEligibility
import com.tokopedia.shop.flashsale.domain.entity.aggregate.CampaignPrerequisiteData
import com.tokopedia.shop.flashsale.domain.entity.aggregate.ShareComponentMetadata
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignListUseCase
import com.tokopedia.shop.flashsale.domain.usecase.aggregate.GenerateCampaignBannerUseCase
import com.tokopedia.shop.flashsale.domain.usecase.aggregate.GetCampaignPrerequisiteDataUseCase
import com.tokopedia.shop.flashsale.domain.usecase.aggregate.GetShareComponentMetadataUseCase
import com.tokopedia.shop.flashsale.domain.usecase.aggregate.ValidateCampaignCreationEligibilityUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class CampaignListViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getSellerCampaignListUseCase: GetSellerCampaignListUseCase,
    private val getCampaignPrerequisiteDataUseCase: GetCampaignPrerequisiteDataUseCase,
    private val getShareComponentMetadataUseCase: GetShareComponentMetadataUseCase,
    private val validateCampaignCreationEligibility: ValidateCampaignCreationEligibilityUseCase,
    private val generateCampaignBannerUseCase: GenerateCampaignBannerUseCase,
    private val getShopPageHomeTypeUseCase : GqlShopPageGetHomeType,
    private val userSession: UserSessionInterface,
    private val tracker: ShopFlashSaleTracker
) : BaseViewModel(dispatchers.main) {

    private val _campaigns = MutableLiveData<Result<CampaignMeta>>()
    val campaigns: LiveData<Result<CampaignMeta>>
        get() = _campaigns

    private val _campaignPrerequisiteData = MutableLiveData<Result<CampaignPrerequisiteData>>()
    val campaignPrerequisiteData: LiveData<Result<CampaignPrerequisiteData>>
        get() = _campaignPrerequisiteData

    private val _shareComponentThumbnailImageUrl = MutableLiveData<Result<String>>()
    val shareComponentThumbnailImageUrl: LiveData<Result<String>>
        get() = _shareComponentThumbnailImageUrl

    private val _shareComponentMetadata = MutableLiveData<Result<ShareComponentMetadata>>()
    val shareComponentMetadata: LiveData<Result<ShareComponentMetadata>>
        get() = _shareComponentMetadata

    private val _sellerEligibility = MutableLiveData<Result<CampaignCreationEligibility>>()
    val creationEligibility: LiveData<Result<CampaignCreationEligibility>>
        get() = _sellerEligibility

    private val _shopDecorStatus = MutableLiveData<Result<String>>()
    val shopDecorStatus: LiveData<Result<String>>
        get() = _shopDecorStatus

    private var drafts: List<CampaignUiModel> = emptyList()
    private var campaignId: Long = 0
    private var thumbnailImageUrl = ""

    fun getCampaigns(
        rows: Int,
        offset: Int,
        statusId: List<Int>,
        campaignName: String,
    ) {
        launchCatchError(
            dispatchers.io,
            block = {
                val campaigns = getSellerCampaignListUseCase.execute(
                    rows = rows,
                    offset = offset,
                    statusId = statusId,
                    campaignId = if (campaignName.isNumber()) campaignName.digitsOnly() else 0,
                    campaignName = if (campaignName.isNumber()) "" else campaignName
                )
                _campaigns.postValue(Success(campaigns))
            },
            onError = { error ->
                _campaigns.postValue(Fail(error))
            }
        )

    }

    fun getCampaignPrerequisiteData() {
        launchCatchError(
            dispatchers.io,
            block = {
                val prerequisiteData = getCampaignPrerequisiteDataUseCase.execute()
                _campaignPrerequisiteData.postValue(Success(prerequisiteData))
            },
            onError = { error ->
                _campaignPrerequisiteData.postValue(Fail(error))
            }
        )

    }

    private fun getShareComponentThumbnailImageUrl(campaignId: Long) {
        launchCatchError(
            dispatchers.io,
            block = {
                val metadata = generateCampaignBannerUseCase.execute(campaignId)
                _shareComponentThumbnailImageUrl.postValue(Success(metadata))
            },
            onError = { error ->
                _shareComponentThumbnailImageUrl.postValue(Fail(error))
            }
        )
    }

    fun getShareComponentMetadata(campaignId: Long) {
        launchCatchError(
            dispatchers.io,
            block = {
                val metadata = getShareComponentMetadataUseCase.execute(campaignId)
                _shareComponentMetadata.postValue(Success(metadata))
            },
            onError = { error ->
                _shareComponentMetadata.postValue(Fail(error))
            }
        )
    }

    fun validateCampaignCreationEligibility() {
        launchCatchError(
            dispatchers.io,
            block = {
                val metadata = validateCampaignCreationEligibility.execute()
                _sellerEligibility.postValue(Success(metadata))
            },
            onError = { error ->
                _sellerEligibility.postValue(Fail(error))
            }
        )

    }


    fun getShopDecorStatus() {
        tracker.sendClickCreateCampaignEvent()

        launchCatchError(
            dispatchers.io,
            block = {
                val requestParams = GqlShopPageGetHomeType.createParams(userSession.shopId, extParam = "")
                getShopPageHomeTypeUseCase.params = requestParams
                getShopPageHomeTypeUseCase.isFromCacheFirst = false
                val shopHome = getShopPageHomeTypeUseCase.executeOnBackground()
                val shopHomeType = shopHome.shopHomeType
                _shopDecorStatus.postValue(Success(shopHomeType))
            },
            onError = { error ->
                _shopDecorStatus.postValue(Fail(error))
            }
        )

    }

    fun setCampaignDrafts(drafts: List<CampaignUiModel>) {
        this.drafts = drafts
    }

    fun getCampaignDrafts(): List<CampaignUiModel> {
        return drafts
    }

    fun setSelectedCampaignId(campaignId: Long) {
        this.campaignId = campaignId
    }

    fun getSelectedCampaignId(): Long {
        return campaignId
    }

    fun setThumbnailImageUrl(thumbnailImageUrl: String) {
        this.thumbnailImageUrl = thumbnailImageUrl
    }

    fun getThumbnailImageUrl(): String {
        return thumbnailImageUrl
    }

    fun onMoreMenuViewCampaignDetailClicked() {
        tracker.sendClickViewCampaignDetailPopUpEvent()
    }

    fun onMoreMenuCancelClicked(campaign: CampaignUiModel) {
        tracker.sendClickCancelPopupEvent(campaign)
    }

    fun onMoreMenuStopClicked(campaign: CampaignUiModel) {
        tracker.sendClickStopPopupEvent(campaign)
    }

    fun onMoreMenuShareClicked(campaign: CampaignUiModel) {
        getShareComponentThumbnailImageUrl(campaign.campaignId)
        tracker.sendClickShareCampaignPopupEvent(campaign)
    }

    fun onMoreMenuEditClicked(campaign: CampaignUiModel) {
        tracker.sendClickEditPopupEvent(campaign)
    }

}
