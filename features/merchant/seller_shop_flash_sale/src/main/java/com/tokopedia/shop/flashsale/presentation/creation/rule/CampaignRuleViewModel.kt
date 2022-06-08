package com.tokopedia.shop.flashsale.presentation.creation.rule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import javax.inject.Inject

class CampaignRuleViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _selectedPaymentMethod = MutableLiveData<PaymentMethod>(PaymentMethod.None)
    val selectedPaymentMethod: LiveData<PaymentMethod>
        get() = _selectedPaymentMethod

    private val _requireUniqueAccount = MutableLiveData<Boolean>()
    val requireUniqueAccount : LiveData<Boolean>
        get() = _requireUniqueAccount

    fun onRegularPaymentMethodSelected() {
        _selectedPaymentMethod.value = PaymentMethod.Regular
    }

    fun onInstantPaymentMethodSelected() {
        _selectedPaymentMethod.value = PaymentMethod.Instant
    }

    fun onRequireUniqueAccountSelected() {
        _requireUniqueAccount.value = true
    }

    fun onNotRequireUniqueAccountSelected() {
        _requireUniqueAccount.value = false
    }
}