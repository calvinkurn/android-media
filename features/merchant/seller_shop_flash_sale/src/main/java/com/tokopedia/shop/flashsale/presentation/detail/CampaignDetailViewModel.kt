package com.tokopedia.shop.flashsale.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.flashsale.domain.entity.CampaignUiModel
import com.tokopedia.shop.flashsale.domain.entity.MerchantCampaignTNC
import com.tokopedia.shop.flashsale.domain.entity.enums.isActive
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignDetailUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import javax.inject.Inject

class CampaignDetailViewModel @Inject constructor(
    private val getSellerCampaignDetailUseCase: GetSellerCampaignDetailUseCase,
    private val dispatchers: CoroutineDispatchers,
) : BaseViewModel(dispatchers.main) {

    companion object {
        private const val INVALID_CAMPAIGN_ID = -1L
    }

    private val _campaign = MutableLiveData<Result<CampaignUiModel>>()
    val campaign: LiveData<Result<CampaignUiModel>>
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

    fun getCampaignDetail(campaignId: Long) {
        this._campaignId = campaignId
        fetchCampaignDetail()
    }

    private fun fetchCampaignDetail() {
        launchCatchError(
            dispatchers.io,
            block = {
                val campaign = getSellerCampaignDetailUseCase.execute(
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
        val campaignData = if (campaign is Success) campaign.data else return
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
        val campaignData = if (campaign is Success) campaign.data else return
        if (campaignData.thematicParticipation) {
            _editCampaignActionResult.value = EditCampaignActionResult.RegisteredEventCampaign
        } else {
            _editCampaignActionResult.value = EditCampaignActionResult.Allowed(_campaignId)
        }
    }

    fun onMoreMenuClicked() {
        val campaign = _campaign.value
        val campaignData = if (campaign is Success) campaign.data else return
        _moreMenuEvent.postValue(campaignData)
    }

    fun onCampaignCancelMenuClicked() {
        val campaign = _campaign.value
        val campaignData = if (campaign is Success) campaign.data else return
        if (campaignData.thematicParticipation) {
            _cancelCampaignActionResult.value = CancelCampaignActionResult.RegisteredEventCampaign
        } else if (campaignData.status.isActive()) {
            _cancelCampaignActionResult.value = CancelCampaignActionResult.ActionAllowed(campaignData)
        }
    }
}