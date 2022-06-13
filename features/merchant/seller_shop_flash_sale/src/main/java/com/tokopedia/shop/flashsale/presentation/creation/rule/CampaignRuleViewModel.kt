package com.tokopedia.shop.flashsale.presentation.creation.rule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.flashsale.domain.entity.MerchantCampaignTNC
import com.tokopedia.shop.flashsale.domain.entity.RelatedCampaign
import com.tokopedia.shop.flashsale.domain.entity.enums.PaymentType
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import javax.inject.Inject

class CampaignRuleViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    companion object {
        private const val MAX_RELATED_CAMPAIGN = 10
    }

    private val _selectedPaymentType = MutableLiveData<PaymentType>()
    val selectedPaymentType: LiveData<PaymentType>
        get() = _selectedPaymentType

    private val _requireUniqueAccount = MutableLiveData<Boolean>()
    val requireUniqueAccount: LiveData<Boolean>
        get() = _requireUniqueAccount

    private val _allowCampaignRelation = MutableLiveData<Boolean>()
    val allowCampaignRelation: LiveData<Boolean>
        get() = _allowCampaignRelation

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

    init {
        initRelatedCampaignButtonMediator()
        initInputValidationMediator()
        initCampaignCreationAllowedMediator()
    }

    private fun initRelatedCampaignButtonMediator() {
        _isRelatedCampaignsButtonActive.addSource(_allowCampaignRelation) {
            _isRelatedCampaignsButtonActive.value = !it
        }
        _isRelatedCampaignsButtonActive.addSource(_relatedCampaigns) {
            _isRelatedCampaignsButtonActive.value = isRelatedCampaignBelowMaximum(it)
        }
    }

    private val isSelectedPaymentTypeValid: Boolean
        get() = selectedPaymentType.value != null
    private val isUniqueAccountValid: Boolean
        get() = requireUniqueAccount.value != null
    private val isRelatedCampaignValid: Boolean
        get() {
            return allowCampaignRelation.value != null
        }

    private fun initInputValidationMediator() {
        _isAllInputValid.addSource(_selectedPaymentType) {
            _isAllInputValid.value = it != null
                    && isUniqueAccountValid
                    && isRelatedCampaignValid
        }
        _isAllInputValid.addSource(_requireUniqueAccount) {
            _isAllInputValid.value = it != null
                    && isSelectedPaymentTypeValid
                    && isRelatedCampaignValid
        }
        _isAllInputValid.addSource(_allowCampaignRelation) {
            _isAllInputValid.value = it != null
                    && isSelectedPaymentTypeValid
                    && isUniqueAccountValid
        }
    }

    private fun initCampaignCreationAllowedMediator() {
        _isCampaignCreationAllowed.addSource(isAllInputValid) {
            _isCampaignCreationAllowed.value = it && isTNCConfirmed.value == true
        }
        _isCampaignCreationAllowed.addSource(isTNCConfirmed) {
            _isCampaignCreationAllowed.value = it && isAllInputValid.value == true
        }
    }

    private fun isRelatedCampaignBelowMaximum(relatedCampaigns: List<RelatedCampaign>): Boolean {
        return _allowCampaignRelation.value == false && relatedCampaigns.size < MAX_RELATED_CAMPAIGN
    }

    fun onRegularPaymentMethodSelected() {
        _selectedPaymentType.value = PaymentType.REGULAR
    }

    fun onInstantPaymentMethodSelected() {
        _selectedPaymentType.value = PaymentType.INSTANT
    }

    fun onRequireUniqueAccountSelected() {
        _requireUniqueAccount.value = true
    }

    fun onNotRequireUniqueAccountSelected() {
        _requireUniqueAccount.value = false
    }

    fun onAllowCampaignRelation() {
        _allowCampaignRelation.value = true
        _relatedCampaigns.value = emptyList()
    }

    fun onDisallowCampaignRelation() {
        _allowCampaignRelation.value = false
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
        val isUniqueBuyer = requireUniqueAccount.value == false
        val isCampaignRelation = allowCampaignRelation.value == true
        _tncClickEvent.value = MerchantCampaignTNC.TncRequest(
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
}