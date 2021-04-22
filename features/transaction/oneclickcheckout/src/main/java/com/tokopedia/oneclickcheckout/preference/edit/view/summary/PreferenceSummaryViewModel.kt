package com.tokopedia.oneclickcheckout.preference.edit.view.summary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.oneclickcheckout.common.idling.OccIdlingResource
import com.tokopedia.oneclickcheckout.common.view.model.Failure
import com.tokopedia.oneclickcheckout.common.view.model.OccState
import com.tokopedia.oneclickcheckout.common.view.model.preference.AddressModel
import com.tokopedia.oneclickcheckout.common.view.model.preference.ProfilesItemModel
import com.tokopedia.oneclickcheckout.preference.edit.domain.create.CreatePreferenceUseCase
import com.tokopedia.oneclickcheckout.preference.edit.domain.create.model.CreatePreferenceRequest
import com.tokopedia.oneclickcheckout.preference.edit.domain.delete.DeletePreferenceUseCase
import com.tokopedia.oneclickcheckout.preference.edit.domain.get.GetPreferenceByIdUseCase
import com.tokopedia.oneclickcheckout.preference.edit.domain.update.UpdatePreferenceUseCase
import com.tokopedia.oneclickcheckout.preference.edit.domain.update.model.UpdatePreferenceRequest
import com.tokopedia.oneclickcheckout.preference.edit.view.PreferenceEditActivity
import com.tokopedia.purchase_platform.common.feature.localizationchooseaddress.request.ChosenAddress
import javax.inject.Inject

class PreferenceSummaryViewModel @Inject constructor(private val getPreferenceByIdUseCase: GetPreferenceByIdUseCase,
                                                     private val createPreferenceUseCase: CreatePreferenceUseCase,
                                                     private val deletePreferenceUseCase: DeletePreferenceUseCase,
                                                     private val updatePreferenceUseCase: UpdatePreferenceUseCase) : ViewModel() {

    private val _preference: MutableLiveData<OccState<ProfilesItemModel>> = MutableLiveData()
    val preference: LiveData<OccState<ProfilesItemModel>>
        get() = _preference

    private val _editResult: MutableLiveData<OccState<String>> = MutableLiveData()
    val editResult: LiveData<OccState<String>>
        get() = _editResult

    private val _localCacheAddressResult: MutableLiveData<AddressModel> = MutableLiveData()
    val localCacheAddressResult: LiveData<AddressModel>
        get() = _localCacheAddressResult

    private var profileAddressId: Int = 0
    private var profileServiceId: Int = 0
    private var profileGatewayCode: String = ""
    private var profilePaymentMetadata: String = ""

    fun setProfileAddressId(profileAddressId: Int) {
        this.profileAddressId = profileAddressId
    }

    fun setProfileServiceId(profileServiceId: Int) {
        this.profileServiceId = profileServiceId
    }

    fun setProfileGatewayCode(profileGatewayCode: String) {
        this.profileGatewayCode = profileGatewayCode
    }

    fun setProfilePaymentMetadata(profilePaymentMetadata: String) {
        this.profilePaymentMetadata = profilePaymentMetadata
    }

    fun isDataChanged(): Boolean {
        val currentPref = _preference.value
        if (currentPref is OccState.Success) {
            return when {
                currentPref.data.addressModel.addressId != profileAddressId -> true
                currentPref.data.shipmentModel.serviceId != profileServiceId -> true
                currentPref.data.paymentModel.gatewayCode != profileGatewayCode -> true
                currentPref.data.paymentModel.metadata != profilePaymentMetadata -> true
                else -> false
            }
        }
        return false
    }

    fun getPreferenceDetail(profileId: Int, addressId: Int, serviceId: Int, gatewayCode: String, metadata: String, paymentProfile: String, fromFlow: String) {
        _preference.value = OccState.Loading
        OccIdlingResource.increment()
        getPreferenceByIdUseCase.execute(
                { profilesItemModel: ProfilesItemModel ->
                    _preference.value = OccState.Success(profilesItemModel)
                    OccIdlingResource.decrement()
                },
                { throwable: Throwable ->
                    _preference.value = OccState.Failed(Failure(throwable))
                    OccIdlingResource.decrement()
                },
                getPreferenceByIdUseCase.generateRequestParams(profileId, addressId, serviceId, gatewayCode, metadata, paymentProfile, fromFlow))
    }

    fun deletePreference(id: Int) {
        val value = _preference.value
        if (value is OccState.Success) {
            _editResult.value = OccState.Loading
            OccIdlingResource.increment()
            deletePreferenceUseCase.execute(id,
                    { message: String ->
                        _editResult.value = OccState.Success(message)
                        OccIdlingResource.decrement()
                    },
                    { throwable: Throwable ->
                        _editResult.value = OccState.Failed(Failure(throwable))
                        OccIdlingResource.decrement()
                    })
        }
    }

    fun createPreference(addressId: Int, serviceId: Int, gatewayCode: String, paymentQuery: String, isDefaultProfileChecked: Boolean, fromFlow: Int, addressModel: AddressModel?, isSelectedPreference: Boolean) {
        _editResult.value = OccState.Loading
        OccIdlingResource.increment()
        var chosenAddress: ChosenAddress? = null
        addressModel?.let {
            chosenAddress = ChosenAddress(
                    addressId = addressModel.addressId.toString(),
                    districtId = addressModel.districtId.toString(),
                    postalCode = addressModel.postalCode,
                    geolocation = if (addressModel.latitude.isNotBlank() && addressModel.longitude.isNotBlank()) addressModel.latitude + "," + addressModel.longitude else "",
                    mode = ChosenAddress.MODE_ADDRESS
            )
        }
        createPreferenceUseCase.execute(CreatePreferenceRequest(addressId, serviceId, gatewayCode, paymentQuery, isDefaultProfileChecked, fromFlow, chosenAddress),
                { message: String ->
                    if (addressModel != null && fromFlow == PreferenceEditActivity.FROM_FLOW_OSP && (isSelectedPreference || isDefaultProfileChecked)) {
                        _localCacheAddressResult.value = addressModel
                    }
                    _editResult.value = OccState.Success(message)
                    OccIdlingResource.decrement()
                },
                { throwable: Throwable ->
                    _editResult.value = OccState.Failed(Failure(throwable))
                    OccIdlingResource.decrement()
                })
    }

    fun updatePreference(profileId: Int, addressId: Int, serviceId: Int, gatewayCode: String, paymentQuery: String, isDefaultProfileChecked: Boolean, fromFlow: Int, addressModel: AddressModel?, isSelectedPreference: Boolean) {
        _editResult.value = OccState.Loading
        OccIdlingResource.increment()
        updatePreferenceUseCase.execute(UpdatePreferenceRequest(profileId, addressId, serviceId, gatewayCode, paymentQuery, isDefaultProfileChecked, fromFlow),
                { message: String ->
                    if (addressModel != null && fromFlow == PreferenceEditActivity.FROM_FLOW_OSP && (isSelectedPreference || isDefaultProfileChecked)) {
                        _localCacheAddressResult.value = addressModel
                    }
                    _editResult.value = OccState.Success(message)
                    OccIdlingResource.decrement()
                },
                { throwable: Throwable ->
                    _editResult.value = OccState.Failed(Failure(throwable))
                    OccIdlingResource.decrement()
                })
    }
}