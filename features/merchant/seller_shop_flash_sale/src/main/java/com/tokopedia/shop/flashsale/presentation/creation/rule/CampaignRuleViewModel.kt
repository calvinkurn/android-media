package com.tokopedia.shop.flashsale.presentation.creation.rule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.flashsale.domain.entity.CampaignAction
import com.tokopedia.shop.flashsale.domain.entity.CampaignUiModel
import com.tokopedia.shop.flashsale.domain.entity.MerchantCampaignTNC
import com.tokopedia.shop.flashsale.domain.entity.RelatedCampaign
import com.tokopedia.shop.flashsale.domain.entity.enums.PaymentType
import com.tokopedia.shop.flashsale.domain.usecase.DoSellerCampaignCreationUseCase
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignDetailUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import javax.inject.Inject

class CampaignRuleViewModel @Inject constructor(
    private val getSellerCampaignDetailUseCase: GetSellerCampaignDetailUseCase,
    private val doSellerCampaignCreationUseCase: DoSellerCampaignCreationUseCase,
    private val dispatchers: CoroutineDispatchers,
) : BaseViewModel(dispatchers.main) {

    companion object {
        private const val MAX_RELATED_CAMPAIGN = 10
        private const val INVALID_CAMPAIGN_ID = -1L
    }

    private val _campaign = MutableLiveData<Result<CampaignUiModel>>()
    val campaign: LiveData<Result<CampaignUiModel>>
        get() = _campaign

    private val _selectedPaymentType = MutableLiveData<PaymentType>()
    val selectedPaymentType: LiveData<PaymentType>
        get() = _selectedPaymentType

    private val _isUniqueBuyer = MutableLiveData<Boolean>()
    val isUniqueBuyer: LiveData<Boolean>
        get() = _isUniqueBuyer

    private val _isCampaignRelation = MutableLiveData<Boolean>()
    val isCampaignRelation: LiveData<Boolean>
        get() = _isCampaignRelation

    private val _relatedCampaigns = MutableLiveData<List<RelatedCampaign>>()
    val relatedCampaigns: LiveData<List<RelatedCampaign>>
        get() = _relatedCampaigns

    private val _isRelatedCampaignsButtonActive = MediatorLiveData<Boolean>()
    val isRelatedCampaignButtonActive: LiveData<Boolean>
        get() = _isRelatedCampaignsButtonActive

    private val _isRelatedCampaignRemoved = SingleLiveEvent<Boolean>()
    val isRelatedCampaignRemoved: LiveData<Boolean>
        get() = _isRelatedCampaignRemoved

    private val _isAllInputValid = MediatorLiveData<Boolean>()
    val isAllInputValid: LiveData<Boolean>
        get() = _isAllInputValid

    private val _tncClickEvent = SingleLiveEvent<MerchantCampaignTNC.TncRequest>()
    val tncClickEvent: LiveData<MerchantCampaignTNC.TncRequest>
        get() = _tncClickEvent

    private val _isTNCConfirmed = MutableLiveData<Boolean>()
    val isTNCConfirmed: LiveData<Boolean>
        get() = _isTNCConfirmed

    private val _isCampaignCreationAllowed = MediatorLiveData<Boolean>()
    val isCampaignCreationAllowed: LiveData<Boolean>
        get() = _isCampaignCreationAllowed

    private var campaignId: Long = INVALID_CAMPAIGN_ID

    private val _saveDraftState = MutableLiveData<Result<Unit>>()
    val saveDraftState: LiveData<Result<Unit>>
        get() = _saveDraftState

    private var initialPaymentType: PaymentType? = null
    private var initialUniqueBuyer: Boolean? = null
    private var initialCampaignRelation: Boolean? = null
    private var initialCampaignRelations: List<RelatedCampaign> = emptyList()

    private val isInputChanged: Boolean
        get() {
            val paymentTypeValue = selectedPaymentType.value
            val isUniqueBuyerValue = isUniqueBuyer.value
            val isCampaignRelationValue = isCampaignRelation.value
            val relatedCampaignsValue = relatedCampaigns.value
            return initialPaymentType?.id != paymentTypeValue?.id
                    || initialUniqueBuyer != isUniqueBuyerValue
                    || initialCampaignRelation != isCampaignRelationValue
                    || initialCampaignRelations != relatedCampaignsValue
        }

    init {
        initRelatedCampaignButtonMediator()
        initInputValidationMediator()
        initCampaignCreationAllowedMediator()
    }

    private fun resetTNCConfirmationStatusIfDataChanged() {
        if (isInputChanged) onTNCCheckboxUnchecked()
    }

    fun getCampaignDetail(campaignId: Long) {
        this.campaignId = campaignId
        fetchCampaignDetail()
    }

    private fun fetchCampaignDetail() {
        launchCatchError(
            dispatchers.io,
            block = {
                val campaign = getSellerCampaignDetailUseCase.execute(
                    campaignId = campaignId
                )
                if (campaign.isCampaignRuleSubmit) {
                    _selectedPaymentType.postValue(campaign.paymentType)
                    initialPaymentType = campaign.paymentType
                    _isUniqueBuyer.postValue(campaign.isUniqueBuyer)
                    initialUniqueBuyer = campaign.isUniqueBuyer
                    if (!campaign.isUniqueBuyer) {
                        _isCampaignRelation.postValue(campaign.isCampaignRelation)
                        initialCampaignRelation = campaign.isCampaignRelation
                        _relatedCampaigns.postValue(campaign.relatedCampaigns)
                        initialCampaignRelations = campaign.relatedCampaigns
                    }
                }
                _isTNCConfirmed.postValue(campaign.isCampaignRuleSubmit)
                _campaign.postValue(Success(campaign))
            },
            onError = { error ->
                _campaign.postValue(Fail(error))
            }
        )
    }

    fun reFetchCampaignDetail() {
        if (campaignId == INVALID_CAMPAIGN_ID) return
        fetchCampaignDetail()
    }

    private fun initRelatedCampaignButtonMediator() {
        _isRelatedCampaignsButtonActive.addSource(_isCampaignRelation) {
            _isRelatedCampaignsButtonActive.value = it == true
        }
        _isRelatedCampaignsButtonActive.addSource(_relatedCampaigns) {
            _isRelatedCampaignsButtonActive.value = isRelatedCampaignBelowMaximum(it)
        }
    }

    private val isSelectedPaymentTypeValid: Boolean
        get() = selectedPaymentType.value != null
    private val isUniqueAccountValid: Boolean
        get() = isUniqueBuyer.value != null

    private fun isRelatedCampaignValid(
        isUniqueAccountValue: Boolean? = isUniqueBuyer.value,
        isCampaignRelationValue: Boolean? = isCampaignRelation.value,
        relatedCampaignsValue: List<RelatedCampaign>? = relatedCampaigns.value,
    ): Boolean {
        return isUniqueAccountValue == true
                || isCampaignRelationValue == true
                || (isCampaignRelationValue == false && relatedCampaignsValue?.isNotEmpty() == true)
    }

    private fun initInputValidationMediator() {
        _isAllInputValid.value = isSelectedPaymentTypeValid
                && isUniqueAccountValid
                && isRelatedCampaignValid()

        _isAllInputValid.addSource(selectedPaymentType) {
            _isAllInputValid.value = it != null
                    && isUniqueAccountValid
                    && isRelatedCampaignValid()
            resetTNCConfirmationStatusIfDataChanged()
        }
        _isAllInputValid.addSource(isUniqueBuyer) {
            _isAllInputValid.value = it != null
                    && isSelectedPaymentTypeValid
                    && isRelatedCampaignValid(it)
            resetTNCConfirmationStatusIfDataChanged()
        }
        _isAllInputValid.addSource(isCampaignRelation) {
            _isAllInputValid.value = isRelatedCampaignValid(isCampaignRelationValue = it)
                    && isSelectedPaymentTypeValid
                    && isUniqueAccountValid
            resetTNCConfirmationStatusIfDataChanged()
        }
        _isAllInputValid.addSource(relatedCampaigns) {
            _isAllInputValid.value = isRelatedCampaignValid(relatedCampaignsValue = it)
                    && isSelectedPaymentTypeValid
                    && isUniqueAccountValid
            resetTNCConfirmationStatusIfDataChanged()
        }
    }

    private fun initCampaignCreationAllowedMediator() {
        _isCampaignCreationAllowed.value =
            isAllInputValid.value == true && isTNCConfirmed.value == true
        _isCampaignCreationAllowed.addSource(isAllInputValid) {
            _isCampaignCreationAllowed.value = it && isTNCConfirmed.value == true
        }
        _isCampaignCreationAllowed.addSource(isTNCConfirmed) {
            _isCampaignCreationAllowed.value = it && isAllInputValid.value == true
        }
    }

    private fun isRelatedCampaignBelowMaximum(
        relatedCampaigns: List<RelatedCampaign>
    ): Boolean {
        return _isCampaignRelation.value == false && relatedCampaigns.size < MAX_RELATED_CAMPAIGN
    }

    fun onRegularPaymentMethodSelected() {
        _selectedPaymentType.value = PaymentType.REGULAR
    }

    fun onInstantPaymentMethodSelected() {
        _selectedPaymentType.value = PaymentType.INSTANT
    }

    fun onRequireUniqueAccountSelected() {
        _isUniqueBuyer.value = false
    }

    fun onNotRequireUniqueAccountSelected() {
        _isUniqueBuyer.value = true
    }

    fun onAllowCampaignRelation() {
        _isCampaignRelation.value = true
        _relatedCampaigns.value = emptyList()
    }

    fun onDisallowCampaignRelation() {
        _isCampaignRelation.value = false
    }

    fun onRemoveRelatedCampaign(relatedCampaign: RelatedCampaign) {
        _relatedCampaigns.value = _relatedCampaigns.value?.minus(relatedCampaign)
        _isRelatedCampaignRemoved.value = true
    }

    fun onRelatedCampaignsChanged(
        relatedCampaigns: List<RelatedCampaign>
    ) {
        _relatedCampaigns.value = relatedCampaigns
    }

    fun onTNCCheckboxChecked() {
        val paymentType = selectedPaymentType.value ?: return
        val isUniqueBuyer = isUniqueBuyer.value == true
        val isCampaignRelation = !isUniqueBuyer && isCampaignRelation.value == true
        _tncClickEvent.value = MerchantCampaignTNC.TncRequest(
            campaignId = campaignId,
            isUniqueBuyer = isUniqueBuyer,
            isCampaignRelation = isCampaignRelation,
            paymentType = paymentType,
        )
    }

    fun onTNCCheckboxUnchecked() {
        _isTNCConfirmed.value = false
    }

    fun onTNCConfirmationClicked() {
        _isTNCConfirmed.value = true
    }

    fun saveCampaignCreationDraft() {
        if (isCampaignCreationAllowed.value == false) {
            _saveDraftState.postValue(Fail(Throwable("Campaign Rule invalid")))
            return
        }
        val campaignValue = campaign.value
        val campaignData = if (campaignValue is Success) campaignValue.data else {
            _saveDraftState.postValue(Fail(Throwable("Campaign Data not found")))
            return
        }
        launchCatchError(
            dispatchers.io,
            block = {
                val param = getCampaignCreationParam(campaignData)
                val result = doSellerCampaignCreationUseCase.execute(param)
                if (result.isSuccess) {
                    _saveDraftState.postValue(Success(Unit))
                } else {
                    _saveDraftState.postValue(Fail(Throwable(result.errorDescription)))
                }
            },
            onError = { error ->
                _saveDraftState.postValue(Fail(error))
            }
        )
    }

    private fun getCampaignCreationParam(campaignData: CampaignUiModel): DoSellerCampaignCreationUseCase.Param {
        val campaignRelations = getCampaignRelationIds(campaignId)
        val selectedPaymentType = selectedPaymentType.value ?: campaignData.paymentType
        val isCampaignRuleSubmit = isTNCConfirmed.value == true
        return DoSellerCampaignCreationUseCase.Param(
            action = CampaignAction.Update(campaignId),
            campaignName = campaignData.campaignName,
            scheduledStart = campaignData.startDate,
            scheduledEnd = campaignData.endDate,
            teaserDate = campaignData.upcomingDate,
            firstColor = campaignData.gradientColor.first,
            secondColor = campaignData.gradientColor.second,
            campaignRelation = campaignRelations,
            paymentType = selectedPaymentType,
            isCampaignRuleSubmit = isCampaignRuleSubmit,
        )
    }

    private fun getCampaignRelationIds(campaignId: Long): List<Long> {
        return when {
            isUniqueBuyer.value == true -> {
                emptyList()
            }
            isCampaignRelation.value == false -> {
                relatedCampaigns.value?.map { it.id } ?: emptyList()
            }
            else -> {
                listOf(campaignId)
            }
        }
    }
}