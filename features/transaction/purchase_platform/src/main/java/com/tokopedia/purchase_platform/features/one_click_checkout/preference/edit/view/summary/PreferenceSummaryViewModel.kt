package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.summary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.OccState
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.domain.create.CreatePreferenceUseCase
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.domain.create.model.CreatePreferenceGqlResponse
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.domain.create.model.CreatePreferenceRequest
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.domain.delete.DeletePreferenceUseCase
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.domain.delete.model.DeletePreferenceGqlResponse
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.domain.get.GetPreferenceByIdUseCase
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.domain.get.model.GetPreferenceData
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.domain.update.UpdatePreferenceUseCase
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.domain.update.model.UpdatePreferenceGqlResponse
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.domain.update.model.UpdatePreferenceRequest
import javax.inject.Inject

class PreferenceSummaryViewModel @Inject constructor(private val getPreferenceByIdUseCase: GetPreferenceByIdUseCase,
                                                     private val createPreferenceUseCase: CreatePreferenceUseCase,
                                                     private val deletePreferenceUseCase: DeletePreferenceUseCase,
                                                     private val updatePreferenceUseCase: UpdatePreferenceUseCase) : ViewModel() {

    private val _preference: MutableLiveData<OccState<GetPreferenceData>> = MutableLiveData()
    val preference: LiveData<OccState<GetPreferenceData>>
        get() = _preference

    private val _editResult: MutableLiveData<OccState<String>> = MutableLiveData()
    val editResult: LiveData<OccState<String>>
        get() = _editResult

    var profileAddressId: Int = 0
    var profileServiceId: Int = 0
    var profileGatewayCode: String = ""
    var profilePaymentMetadata: String = ""

    fun isDataChanged(): Boolean {
        val currentPref = _preference.value
        if (currentPref != null && currentPref is OccState.Success) {
            return when {
                currentPref.data.addressModel?.addressId != profileAddressId -> true
                currentPref.data.shipmentModel?.serviceId != profileServiceId -> true
                currentPref.data.paymentModel?.gatewayCode != profileGatewayCode -> true
                currentPref.data.paymentModel?.metadata != profilePaymentMetadata -> true
                else -> false
            }
        }
        return false
    }

    fun getPreferenceDetail(profileId: Int, addressId: Int, serviceId: Int, gatewayCode: String, metadata: String) {
        _preference.value = OccState.Loading
        getPreferenceByIdUseCase.execute(profileId, addressId, serviceId, gatewayCode, metadata,
                { getPreferenceData: GetPreferenceData ->
                    _preference.value = OccState.Success(getPreferenceData)
                },
                { throwable: Throwable ->
                    _preference.value = OccState.Fail(false, throwable, "")
                })
    }

    fun deletePreference(id: Int) {
        val value = _preference.value
        if (value is OccState.Success) {
            _editResult.value = OccState.Loading
            deletePreferenceUseCase.execute(id, { deletePreferenceGqlResponse: DeletePreferenceGqlResponse ->
                val messages = deletePreferenceGqlResponse.response.data.messages
                if (messages.isNotEmpty()) {
                    _editResult.value = OccState.Success(messages[0])
                } else {
                    _editResult.value = OccState.Success("Success")
                }
            }, { throwable: Throwable ->
                _editResult.value = OccState.Fail(false, throwable, "")
            })
        }
    }

    fun createPreference(addressId: Int, serviceId: Int, gatewayCode: String, paymentQuery: String, isDefaultProfileChecked: Boolean, fromFlow: Int) {
        _editResult.value = OccState.Loading
        createPreferenceUseCase.execute(CreatePreferenceRequest(addressId, serviceId, gatewayCode, paymentQuery, isDefaultProfileChecked, fromFlow),
                { createPreferenceGqlResponse: CreatePreferenceGqlResponse ->
                    val messages = createPreferenceGqlResponse.response.data.messages
                    if (messages.isNotEmpty()) {
                        _editResult.value = OccState.Success(messages[0])
                    } else {
                        _editResult.value = OccState.Success("Success")
                    }
                },
                { throwable: Throwable ->
                    _editResult.value = OccState.Fail(false, throwable, "")
                })
    }

    fun updatePreference(profileId: Int, addressId: Int, serviceId: Int, gatewayCode: String, paymentQuery: String, isDefaultProfileChecked: Boolean, fromFlow: Int) {
        _editResult.value = OccState.Loading
        updatePreferenceUseCase.execute(UpdatePreferenceRequest(profileId, addressId, serviceId, gatewayCode, paymentQuery, isDefaultProfileChecked, fromFlow),
                { updatePreferenceGqlResponse: UpdatePreferenceGqlResponse ->
                    val messages = updatePreferenceGqlResponse.response.data.messages
                    if (messages.isNotEmpty()) {
                        _editResult.value = OccState.Success(messages[0])
                    } else {
                        _editResult.value = OccState.Success("Success")
                    }
                },
                { throwable: Throwable ->
                    _editResult.value = OccState.Fail(false, throwable, "")
                })
    }

    fun consumeEditResultFail() {
        val value = _editResult.value
        if (value is OccState.Fail && !value.isConsumed) {
            _editResult.value = value.copy(isConsumed = true)
        }
    }
}