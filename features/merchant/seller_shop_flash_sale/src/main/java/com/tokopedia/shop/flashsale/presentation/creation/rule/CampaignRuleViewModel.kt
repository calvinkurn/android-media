package com.tokopedia.shop.flashsale.presentation.creation.rule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.campaign.data.response.RollenceGradualRollout
import com.tokopedia.campaign.usecase.RolloutFeatureVariantsUseCase
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.shop.flashsale.common.extension.removeTimeZone
import com.tokopedia.shop.flashsale.common.tracker.ShopFlashSaleTracker
import com.tokopedia.shop.flashsale.domain.entity.CampaignAction
import com.tokopedia.shop.flashsale.domain.entity.CampaignUiModel
import com.tokopedia.shop.flashsale.domain.entity.MerchantCampaignTNC
import com.tokopedia.shop.flashsale.domain.entity.RelatedCampaign
import com.tokopedia.shop.flashsale.domain.entity.enums.PaymentType
import com.tokopedia.shop.flashsale.domain.entity.enums.isDraft
import com.tokopedia.shop.flashsale.domain.usecase.DoSellerCampaignCreationUseCase
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignDetailUseCase
import com.tokopedia.shop.flashsale.domain.usecase.aggregate.ValidateCampaignCreationEligibilityUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

class CampaignRuleViewModel @Inject constructor(
    private val getSellerCampaignDetailUseCase: GetSellerCampaignDetailUseCase,
    private val doSellerCampaignCreationUseCase: DoSellerCampaignCreationUseCase,
    private val validateCampaignCreationEligibilityUseCase: ValidateCampaignCreationEligibilityUseCase,
    private val tracker: ShopFlashSaleTracker,
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    companion object {
        private const val MAX_RELATED_CAMPAIGN = 10
        private const val INVALID_CAMPAIGN_ID = -1L
    }

    private val isInSaveOrCreateAction: AtomicBoolean = AtomicBoolean(false)

    private val _campaign = MutableLiveData<Result<CampaignUiModel>>()
    val campaign: LiveData<Result<CampaignUiModel>>
        get() = _campaign

    private val _selectedPaymentType = MutableLiveData<PaymentType?>(null)
    val selectedPaymentType: LiveData<PaymentType?>
        get() = _selectedPaymentType
    private val selectedPaymentTypeFlow = selectedPaymentType.asFlow()

    private val _selectedOosState = MutableLiveData<Boolean>()
    val selectedOosState: LiveData<Boolean>
        get() = _selectedOosState

    private val _isUniqueBuyer = MutableLiveData<Boolean?>(null)
    val isUniqueBuyer: LiveData<Boolean?>
        get() = _isUniqueBuyer
    private val isUniqueBuyerFlow = isUniqueBuyer.asFlow()

    private val _isCampaignRelation = MutableLiveData<Boolean?>(null)
    val isCampaignRelation: LiveData<Boolean?>
        get() = _isCampaignRelation
    private val isCampaignRelationFlow = isCampaignRelation.asFlow()

    private val _relatedCampaigns = MutableLiveData<List<RelatedCampaign>?>(null)
    val relatedCampaigns: LiveData<List<RelatedCampaign>?>
        get() = _relatedCampaigns
    private val relatedCampaignsFlow = relatedCampaigns.asFlow()

    private val _isRelatedCampaignsVisible = MutableLiveData<Boolean>()
    val isRelatedCampaignsVisible: LiveData<Boolean>
        get() = _isRelatedCampaignsVisible

    private val _isRelatedCampaignsButtonActive = MutableLiveData<Boolean>()
    val isRelatedCampaignButtonActive: LiveData<Boolean>
        get() = _isRelatedCampaignsButtonActive

    private val _isRelatedCampaignRemoved = SingleLiveEvent<Boolean>()
    val isRelatedCampaignRemoved: LiveData<Boolean>
        get() = _isRelatedCampaignRemoved

    private val _isAllInputValid = MutableLiveData<Boolean>()
    val isAllInputValid: LiveData<Boolean>
        get() = _isAllInputValid
    private val isAllInputValidFlow = isAllInputValid.asFlow()

    private val _tncClickEvent = SingleLiveEvent<MerchantCampaignTNC.TncRequest>()
    val tncClickEvent: LiveData<MerchantCampaignTNC.TncRequest>
        get() = _tncClickEvent

    private val _isTNCConfirmed = MutableLiveData<Boolean>()
    val isTNCConfirmed: LiveData<Boolean>
        get() = _isTNCConfirmed
    private val isTNCConfirmedFlow = isTNCConfirmed.asFlow()

    private val _isCampaignCreationAllowed = MutableLiveData<Boolean>()
    val isCampaignCreationAllowed: LiveData<Boolean>
        get() = _isCampaignCreationAllowed

    private var campaignId: Long = INVALID_CAMPAIGN_ID

    private val _saveDraftActionState = MutableLiveData<CampaignRuleActionResult>()
    val saveDraftActionState: LiveData<CampaignRuleActionResult>
        get() = _saveDraftActionState

    private val _createCampaignActionState = MutableLiveData<CampaignRuleActionResult>()
    val createCampaignActionState: LiveData<CampaignRuleActionResult>
        get() = _createCampaignActionState

    private val _addRelatedCampaignButtonEvent = SingleLiveEvent<AddRelatedCampaignRequest>()
    val addRelatedCampaignButtonEvent: LiveData<AddRelatedCampaignRequest>
        get() = _addRelatedCampaignButtonEvent

    private var initialPaymentType: PaymentType? = null
    private var initialOosState: Boolean = true
    private var initialUniqueBuyer: Boolean? = null
    private var initialCampaignRelation: Boolean? = null
    private var initialCampaignRelations: List<RelatedCampaign> = emptyList()
    private var selectedVpsPackageId: Long = 0

    private val isInputChanged: Boolean
        get() {
            val paymentTypeValue = selectedPaymentType.value
            val isOosValue = selectedOosState.value
            val isUniqueBuyerValue = isUniqueBuyer.value
            val isCampaignRelationValue = isCampaignRelation.value
            val relatedCampaignsValue = relatedCampaigns.value
            return initialPaymentType?.id != paymentTypeValue?.id ||
                initialOosState != isOosValue ||
                initialUniqueBuyer != isUniqueBuyerValue ||
                initialCampaignRelation != isCampaignRelationValue ||
                initialCampaignRelations != relatedCampaignsValue
        }

    init {
        initRelatedCampaignButtonCollector()
        initRelatedCampaignGroupVisibilityStatusCollector()
        initInputValidationCollector()
        initCampaignCreationAllowedCollector()
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
                selectedVpsPackageId = campaign.packageInfo.packageId

                if (campaign.isCampaignRuleSubmit) {
                    _selectedPaymentType.postValue(campaign.paymentType)
                    _selectedOosState.postValue(campaign.isOosImprovement)
                    initialPaymentType = campaign.paymentType
                    initialOosState = campaign.isOosImprovement
                    _isUniqueBuyer.postValue(campaign.isUniqueBuyer)
                    initialUniqueBuyer = campaign.isUniqueBuyer
                    if (!campaign.isUniqueBuyer) {
                        _isCampaignRelation.postValue(campaign.isCampaignRelation)
                        initialCampaignRelation = campaign.isCampaignRelation
                        // remove this campaign id from campaign relation data
                        val filteredRelatedCampaigns = campaign.relatedCampaigns.filter {
                            it.id != campaignId
                        }
                        _relatedCampaigns.postValue(filteredRelatedCampaigns)
                        initialCampaignRelations = filteredRelatedCampaigns
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

    private fun initRelatedCampaignButtonCollector() {
        viewModelScope.launch {
            combine(
                isCampaignRelationFlow,
                relatedCampaignsFlow
            ) { isRelatedCampaign, relatedCampaigns ->
                isRelatedCampaign == false &&
                    (relatedCampaigns?.size ?: 0) < MAX_RELATED_CAMPAIGN
            }
                .collect { _isRelatedCampaignsButtonActive.postValue(it) }
        }
    }

    private fun initRelatedCampaignGroupVisibilityStatusCollector() {
        viewModelScope.launch {
            combine(
                isUniqueBuyerFlow,
                isCampaignRelationFlow
            ) { isUniqueBuyer, isCampaignRelation ->
                isUniqueBuyer == false && isCampaignRelation == false
            }
                .collect { _isRelatedCampaignsVisible.postValue(it) }
        }
    }

    private val isSelectedPaymentTypeValid: Boolean
        get() = selectedPaymentType.value != null

    private val isSelectedOosStateTypeValid: Boolean
        get() = selectedOosState.value != null

    private val isUniqueAccountValid: Boolean
        get() = isUniqueBuyer.value != null

    private fun isRelatedCampaignValid(
        isUniqueAccountValue: Boolean? = isUniqueBuyer.value,
        isCampaignRelationValue: Boolean? = isCampaignRelation.value,
        relatedCampaignsValue: List<RelatedCampaign>? = relatedCampaigns.value
    ): Boolean {
        return isUniqueAccountValue == true ||
            isCampaignRelationValue == true ||
            (isCampaignRelationValue == false && relatedCampaignsValue?.isNotEmpty() == true)
    }

    private fun initInputValidationCollector() {
        _isAllInputValid.value = isSelectedPaymentTypeValid &&
            isSelectedOosStateTypeValid &&
            isUniqueAccountValid &&
            isRelatedCampaignValid()

        viewModelScope.launch {
            combine(
                selectedPaymentTypeFlow,
                isUniqueBuyerFlow,
                isCampaignRelationFlow,
                relatedCampaignsFlow,
                ::validateCampaignRuleInput
            )
                .collect { _isAllInputValid.postValue(it) }
        }
    }

    private fun validateCampaignRuleInput(
        selectedPayment: PaymentType?,
        isUniqueBuyer: Boolean?,
        isCampaignRelation: Boolean?,
        relatedCampaigns: List<RelatedCampaign>?
    ): Boolean {
        val validSelectedPayment = selectedPayment != null
        val validUniqueBuyer = isUniqueBuyer != null
        val validCampaignRelation = isUniqueBuyer == true ||
            isCampaignRelation == true ||
            (isCampaignRelation == false && relatedCampaigns?.isNotEmpty() == true)
        resetTNCConfirmationStatusIfDataChanged()
        return validSelectedPayment && validUniqueBuyer && validCampaignRelation
    }

    private fun initCampaignCreationAllowedCollector() {
        _isCampaignCreationAllowed.value = isAllInputValid.value == true &&
            isTNCConfirmed.value == true

        viewModelScope.launch {
            combine(isAllInputValidFlow, isTNCConfirmedFlow) { isAllInputValid, isTNCConfirmed ->
                isAllInputValid && isTNCConfirmed
            }.collect { _isCampaignCreationAllowed.postValue(it) }
        }
    }

    fun setOosStatus(isEnableTransaction: Boolean) {
        _selectedOosState.value = isEnableTransaction
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
    }

    fun onDisallowCampaignRelation() {
        _isCampaignRelation.value = false
    }

    fun onRemoveRelatedCampaign(relatedCampaign: RelatedCampaign) {
        _relatedCampaigns.value = _relatedCampaigns.value?.minus(relatedCampaign)
        _isRelatedCampaignRemoved.value = true
    }

    fun onRelatedCampaignsChanged(relatedCampaigns: List<RelatedCampaign>) {
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
            paymentType = paymentType
        )
    }

    fun onTNCCheckboxUnchecked() {
        _isTNCConfirmed.value = false
    }

    fun onTNCConfirmationClicked() {
        _isTNCConfirmed.value = true
    }

    /**
     * This method should check whether user is already in either save draft or create campaign action,
     * if user is not in those actions, then the [AtomicBoolean.compareAndSet] will update the value to true and then return true,
     * if user is already in those actions, then the [AtomicBoolean.compareAndSet] will not update the value and then return false
     * @return true if user already in save draft or create campaign action, false otherwise
     */
    private fun isAlreadyInSaveOrActionAction(): Boolean {
        return synchronized(this) {
            !isInSaveOrCreateAction.compareAndSet(false, true)
        }
    }

    private fun resetIsInSaveOrCreateAction() {
        synchronized(this) {
            isInSaveOrCreateAction.set(false)
        }
    }

    private fun validateCampaignRuleInput(
        campaign: CampaignUiModel
    ): CampaignRuleValidationResult {
        val currentDate = Calendar.getInstance().time
        val validationResult = when {
            !isSelectedPaymentTypeValid && !isRelatedCampaignValid() -> CampaignRuleValidationResult.BothSectionsInvalid
            !isSelectedPaymentTypeValid -> CampaignRuleValidationResult.InvalidPaymentMethod
            !isRelatedCampaignValid() -> CampaignRuleValidationResult.InvalidBuyerOptions
            isTNCConfirmed.value == false -> CampaignRuleValidationResult.TNCNotAccepted
            campaign.upcomingDate.before(currentDate) -> CampaignRuleValidationResult.InvalidCampaignTime(
                campaign.campaignId
            )
            else -> CampaignRuleValidationResult.Valid
        }
        return validationResult
    }

    fun saveCampaignCreationDraft() {
        if (isAlreadyInSaveOrActionAction()) return
        val campaignValue = campaign.value
        val campaignData = if (campaignValue is Success) {
            campaignValue.data
        } else {
            _saveDraftActionState.postValue(CampaignRuleActionResult.DetailNotLoaded)
            resetIsInSaveOrCreateAction()
            return
        }
        val validationResult = validateCampaignRuleInput(campaignData)
        if (validationResult.isInvalid) {
            _saveDraftActionState.postValue(CampaignRuleActionResult.ValidationFail(validationResult))
            resetIsInSaveOrCreateAction()
            return
        }
        _saveDraftActionState.postValue(CampaignRuleActionResult.Loading)
        launchCatchError(
            dispatchers.io,
            block = {
                val param = getCampaignCreationParam(
                    campaignData,
                    CampaignAction.Update(campaignId)
                )
                val result = doSellerCampaignCreationUseCase.execute(param)
                if (result.isSuccess) {
                    _saveDraftActionState.postValue(CampaignRuleActionResult.Success)
                    resetIsInSaveOrCreateAction()
                } else {
                    _saveDraftActionState.postValue(
                        CampaignRuleActionResult.Fail(
                            CampaignRuleError(result.errorTitle, result.errorMessage)
                        )
                    )
                    resetIsInSaveOrCreateAction()
                }
            },
            onError = { error ->
                _saveDraftActionState.postValue(
                    CampaignRuleActionResult.Fail(
                        CampaignRuleError(cause = error)
                    )
                )
                resetIsInSaveOrCreateAction()
            }
        )
    }

    private fun validateCampaignCreation(
        validAction: suspend (CampaignUiModel) -> Unit
    ) {
        if (isAlreadyInSaveOrActionAction()) return
        val campaignValue = campaign.value
        val campaignData = if (campaignValue is Success) {
            campaignValue.data
        } else {
            _createCampaignActionState.postValue(CampaignRuleActionResult.DetailNotLoaded)
            resetIsInSaveOrCreateAction()
            return
        }
        val validationResult = validateCampaignRuleInput(campaignData)
        if (validationResult.isInvalid) {
            _createCampaignActionState.postValue(
                CampaignRuleActionResult.ValidationFail(
                    validationResult
                )
            )
            resetIsInSaveOrCreateAction()
            return
        }

        _createCampaignActionState.postValue(CampaignRuleActionResult.Loading)
        launchCatchError(
            dispatchers.io,
            block = {
                if (campaignData.status.isDraft()) {
                    val selectedVpsPackageId = campaignData.campaignId
                    val eligibilityResult = validateCampaignCreationEligibilityUseCase.execute(selectedVpsPackageId)
                    if (!eligibilityResult.isEligible) {
                        _createCampaignActionState.postValue(
                            CampaignRuleActionResult.ValidationFail(CampaignRuleValidationResult.NotEligible)
                        )
                        resetIsInSaveOrCreateAction()
                        return@launchCatchError
                    }
                }

                validAction(campaignData)
            },
            onError = { error ->
                _createCampaignActionState.postValue(
                    CampaignRuleActionResult.Fail(
                        CampaignRuleError(cause = error)
                    )
                )
                resetIsInSaveOrCreateAction()
            }
        )
    }

    fun onCreateCampaignButtonClicked() {
        validateCampaignCreation { campaignData ->
            if (campaignData.status.isDraft()) {
                _createCampaignActionState.postValue(CampaignRuleActionResult.ShowConfirmation)
                resetIsInSaveOrCreateAction()
            } else {
                val action = CampaignAction.Update(campaignId)
                doCreateCampaign(campaignData, action)
            }
        }
    }

    fun onCreateCampaignConfirmed() {
        validateCampaignCreation { campaignData ->
            val action = CampaignAction.Submit(campaignId)
            doCreateCampaign(campaignData, action)
        }
    }

    private suspend fun doCreateCampaign(campaignData: CampaignUiModel, action: CampaignAction) {
        val param = getCampaignCreationParam(campaignData, action)
        val result = doSellerCampaignCreationUseCase.execute(param)
        if (result.isSuccess) {
            tracker.sendClickButtonCreateCampaign(campaignId, selectedVpsPackageId)
            _createCampaignActionState.postValue(CampaignRuleActionResult.Success)
            resetIsInSaveOrCreateAction()
        } else {
            _createCampaignActionState.postValue(
                CampaignRuleActionResult.Fail(
                    CampaignRuleError(result.errorTitle, result.errorMessage)
                )
            )
            resetIsInSaveOrCreateAction()
        }
    }

    private fun getCampaignCreationParam(
        campaignData: CampaignUiModel,
        action: CampaignAction
    ): DoSellerCampaignCreationUseCase.Param {
        val campaignRelations = getCampaignRelationIds(campaignId)
        val selectedPaymentType = selectedPaymentType.value ?: campaignData.paymentType
        val isEnableOosTransaction = selectedOosState.value ?: true // The default value should assigned to true -> It's aligned with the UI, if the user didn't do anything, the default selection will be true or can transact if the campaign is out of stock
        val isCampaignRuleSubmit = isTNCConfirmed.value == true
        return DoSellerCampaignCreationUseCase.Param(
            action = action,
            campaignName = campaignData.campaignName,
            scheduledStart = campaignData.startDate.removeTimeZone(),
            scheduledEnd = campaignData.endDate.removeTimeZone(),
            teaserDate = campaignData.upcomingDate.removeTimeZone(),
            firstColor = campaignData.gradientColor.first,
            secondColor = campaignData.gradientColor.second,
            campaignRelation = campaignRelations,
            paymentType = selectedPaymentType,
            isCampaignRuleSubmit = isCampaignRuleSubmit,
            showTeaser = campaignData.useUpcomingWidget,
            packageId = campaignData.packageInfo.packageId,
            isOosImprovement = isEnableOosTransaction
        )
    }

    private fun getCampaignRelationIds(campaignId: Long): List<Long> {
        return when {
            isUniqueBuyer.value == true -> emptyList()
            isCampaignRelation.value == false -> {
                // get selected campaign relations, and then add this campaign id
                relatedCampaigns.value?.map { it.id }?.plus(campaignId) ?: emptyList()
            }
            else -> listOf(campaignId)
        }
    }

    fun onAddRelatedCampaignButtonClicked() {
        _addRelatedCampaignButtonEvent.postValue(
            AddRelatedCampaignRequest(
                campaignId,
                relatedCampaigns.value ?: emptyList()
            )
        )
    }
}
