package com.tokopedia.shop.flashsale.presentation.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.flashsale.common.extension.combineResultWith
import com.tokopedia.shop.flashsale.common.extension.combineWith
import com.tokopedia.shop.flashsale.common.tracker.ShopFlashSaleTracker
import com.tokopedia.shop.flashsale.domain.entity.CampaignDetailMeta
import com.tokopedia.shop.flashsale.domain.entity.CampaignUiModel
import com.tokopedia.shop.flashsale.domain.entity.MerchantCampaignTNC
import com.tokopedia.shop.flashsale.domain.entity.aggregate.ShareComponent
import com.tokopedia.shop.flashsale.domain.entity.aggregate.ShareComponentMetadata
import com.tokopedia.shop.flashsale.domain.entity.enums.isActive
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignDetailUseCase
import com.tokopedia.shop.flashsale.domain.usecase.aggregate.GenerateCampaignBannerUseCase
import com.tokopedia.shop.flashsale.domain.usecase.aggregate.GetCampaignDetailMetaUseCase
import com.tokopedia.shop.flashsale.domain.usecase.aggregate.GetShareComponentMetadataUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import javax.inject.Inject

class CampaignDetailViewModel @Inject constructor(
    private val getCampaignDetailMetaUseCase: GetCampaignDetailMetaUseCase,
    private val tracker: ShopFlashSaleTracker,
    private val getShareComponentMetadataUseCase: GetShareComponentMetadataUseCase,
    private val generateCampaignBannerUseCase: GenerateCampaignBannerUseCase,
    private val dispatchers: CoroutineDispatchers,
) : BaseViewModel(dispatchers.main) {

    companion object {
        private const val INVALID_CAMPAIGN_ID = -1L
    }

    private val _campaign = MutableLiveData<Result<CampaignDetailMeta>>()
    val campaign: LiveData<Result<CampaignDetailMeta>>
        get() = _campaign

    private var _campaignId: Long = INVALID_CAMPAIGN_ID
    val campaignId: Long
        get() = _campaignId

    private val _tncClickEvent = SingleLiveEvent<MerchantCampaignTNC.TncRequest>()
    val tncClickEvent: LiveData<MerchantCampaignTNC.TncRequest>
        get() = _tncClickEvent

    private val _editCampaignActionResult = SingleLiveEvent<EditCampaignActionResult>()
    val editCampaignActionResult: LiveData<EditCampaignActionResult>
        get() = _editCampaignActionResult

    private val _cancelCampaignActionResult = SingleLiveEvent<CancelCampaignActionResult>()
    val cancelCampaignActionResult: LiveData<CancelCampaignActionResult>
        get() = _cancelCampaignActionResult

    private val _moreMenuEvent = SingleLiveEvent<CampaignUiModel>()
    val moreMenuEvent: LiveData<CampaignUiModel>
        get() = _moreMenuEvent

    private val _shareCampaignActionEvent = SingleLiveEvent<CampaignUiModel>()
    val shareCampaignActionEvent = _shareCampaignActionEvent

    private val _shareComponentThumbnailImageUrl = MutableLiveData<Result<String>>()
    val shareComponentThumbnailImageUrl: LiveData<Result<String>>
        get() = _shareComponentThumbnailImageUrl

    private val _shareComponentMetadata = MutableLiveData<Result<ShareComponentMetadata>>()
    val shareComponentMetadata: LiveData<Result<ShareComponentMetadata>>
        get() = _shareComponentMetadata

    private val _shareComponent by lazy {
        shareComponentThumbnailImageUrl.combineResultWith(shareComponentMetadata) { thumbnailImageUrl, metaData ->
            metaData?.let { ShareComponent(thumbnailImageUrl.orEmpty(), it) }
        }
    }
    val shareComponent: LiveData<ShareComponent?>
        get() = _shareComponent

    fun getCampaignDetail(campaignId: Long) {
        this._campaignId = campaignId
        fetchCampaignDetail()
    }

    private fun fetchCampaignDetail() {
        launchCatchError(
            dispatchers.io,
            block = {
                val campaign = getCampaignDetailMetaUseCase.execute(
                    campaignId = _campaignId
                )
                _campaign.postValue(Success(campaign))
            },
            onError = { error ->
                _campaign.postValue(Fail(error))
            }
        )
    }

    fun reFetchCampaignDetail() {
        if (_campaignId == INVALID_CAMPAIGN_ID) return
        fetchCampaignDetail()
    }

    fun onTNCButtonClicked() {
        val campaign = _campaign.value
        val campaignMeta = if (campaign is Success) campaign.data else return
        val campaignData = campaignMeta.campaign
        val paymentType = campaignData.paymentType
        val isUniqueBuyer = campaignData.isUniqueBuyer
        val isCampaignRelation = campaignData.isCampaignRelation
        _tncClickEvent.value = MerchantCampaignTNC.TncRequest(
            campaignId = _campaignId,
            isUniqueBuyer = isUniqueBuyer,
            isCampaignRelation = isCampaignRelation,
            paymentType = paymentType,
        )
    }

    fun onEditCampaignClicked() {
        val campaign = _campaign.value
        val campaignMeta = if (campaign is Success) campaign.data else return
        val campaignData = campaignMeta.campaign
        tracker.sendClickTextEditCampaign(
            campaignData.campaignId,
            campaignData.campaignName,
            campaignData.status
        )
        if (campaignData.thematicParticipation) {
            _editCampaignActionResult.value = EditCampaignActionResult.RegisteredEventCampaign
        } else {
            _editCampaignActionResult.value = EditCampaignActionResult.Allowed(_campaignId)
        }
    }

    fun onMoreMenuClicked() {
        val campaign = _campaign.value
        val campaignMeta = if (campaign is Success) campaign.data else return
        val campaignData = campaignMeta.campaign
        _moreMenuEvent.postValue(campaignData)
    }

    fun onCampaignCancelMenuClicked() {
        val campaign = _campaign.value
        val campaignMeta = if (campaign is Success) campaign.data else return
        val campaignData = campaignMeta.campaign
        if (campaignData.thematicParticipation) {
            _cancelCampaignActionResult.value = CancelCampaignActionResult.RegisteredEventCampaign
        } else if (campaignData.status.isActive()) {
            _cancelCampaignActionResult.value =
                CancelCampaignActionResult.ActionAllowed(campaignData)
        }
    }

    fun onShareButtonClicked() {
        val campaign = _campaign.value
        val campaignMeta = if (campaign is Success) campaign.data else return
        val campaignData = campaignMeta.campaign
        tracker.sendClickButtonShareCampaign(
            campaignData.campaignId,
            campaignData.campaignName,
            campaignData.status
        )
        _shareCampaignActionEvent.value = campaignData
    }

    fun getShareComponent(campaignId: Long) {
        getShareComponentThumbnailImageUrl(campaignId)
        getShareComponentMetadata(campaignId)
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

    private fun getShareComponentMetadata(campaignId: Long) {
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
}