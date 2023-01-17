package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.addressform

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.logisticCommon.data.constant.ManageAddressSource
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.repository.KeroRepository
import com.tokopedia.logisticCommon.data.response.DataAddAddress
import com.tokopedia.logisticCommon.data.response.DefaultAddressData
import com.tokopedia.logisticCommon.data.response.KeroDistrictRecommendation
import com.tokopedia.logisticCommon.data.response.KeroEditAddressResponse
import com.tokopedia.logisticCommon.data.response.KeroGetAddressResponse
import com.tokopedia.logisticCommon.data.response.PinpointValidationResponse
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddressFormViewModel @Inject constructor(private val repo: KeroRepository) : ViewModel() {

    private val _districtDetail = MutableLiveData<Result<KeroDistrictRecommendation>>()
    val districtDetail: LiveData<Result<KeroDistrictRecommendation>>
        get() = _districtDetail

    private val _saveAddress = MutableLiveData<Result<DataAddAddress>>()
    val saveAddress: LiveData<Result<DataAddAddress>>
        get() = _saveAddress

    private val _editAddress = MutableLiveData<Result<KeroEditAddressResponse.Data.KeroEditAddress.KeroEditAddressSuccessResponse>>()
    val editAddress: LiveData<Result<KeroEditAddressResponse.Data.KeroEditAddress.KeroEditAddressSuccessResponse>>
        get() = _editAddress

    private val _defaultAddress = MutableLiveData<Result<DefaultAddressData>>()
    val defaultAddress: LiveData<Result<DefaultAddressData>>
        get() = _defaultAddress

    private val _addressDetail = MutableLiveData<Result<KeroGetAddressResponse.Data>>()
    val addressDetail: LiveData<Result<KeroGetAddressResponse.Data>>
        get() = _addressDetail

    private val _pinpointValidation = MutableLiveData<Result<PinpointValidationResponse.PinpointValidations.PinpointValidationResponseData>>()
    val pinpointValidation: LiveData<Result<PinpointValidationResponse.PinpointValidations.PinpointValidationResponseData>>
        get() = _pinpointValidation

    var source: String = ""
    var isGmsAvailable: Boolean = true
    val isTokonow: Boolean
        get() = source == ManageAddressSource.TOKONOW.source

    fun getDistrictDetail(districtId: String) {
        viewModelScope.launch {
            try {
                val districtDetail = repo.getZipCode(districtId)
                _districtDetail.value = Success(districtDetail.keroDistrictDetails)
            } catch (e: Throwable) {
                _districtDetail.value = Fail(e)
            }
        }
    }

    fun getAddressDetail(addressId: String) {
        viewModelScope.launch {
            try {
                val addressDetail = repo.getAddressDetail(addressId, getSourceValue())
                _addressDetail.value = Success(addressDetail)
            } catch (e: Throwable) {
                _addressDetail.value = Fail(e)
            }
        }
    }

    fun getDefaultAddress(source: String) {
        viewModelScope.launch {
            try {
                val defaultAddress = repo.getDefaultAddress(source)
                _defaultAddress.value = Success(defaultAddress.response.data)
            } catch (e: Throwable) {
                _defaultAddress.value = Fail(e)
            }
        }
    }

    fun saveAddress(model: SaveAddressDataModel) {
        viewModelScope.launch {
            try {
                val saveAddressData = repo.saveAddress(model, getSourceValue())
                _saveAddress.value = Success(saveAddressData.keroAddAddress.data)
            } catch (e: Throwable) {
                _saveAddress.value = Fail(e)
            }
        }
    }

    fun saveEditAddress(model: SaveAddressDataModel) {
        viewModelScope.launch {
            try {
                val editAddressData = repo.editAddress(model, getSourceValue())
                _editAddress.value = Success(editAddressData.keroEditAddress.data)
            } catch (e: Throwable) {
                _editAddress.value = Fail(e)
            }
        }
    }

    private fun getSourceValue(): String {
        return if (isTokonow) {
            ManageAddressSource.LOCALIZED_ADDRESS_WIDGET.source
        } else {
            source
        }
    }

    fun validatePinpoint(model: SaveAddressDataModel) {
        viewModelScope.launch {
            try {
                val pinpointValidationResult = repo.pinpointValidation(model.districtId.toInt(), model.latitude, model.longitude, model.postalCode)
                _pinpointValidation.value = Success(pinpointValidationResult.pinpointValidations.data)
            } catch (e: Throwable) {
                _pinpointValidation.value = Fail(e)
            }
        }
    }
}
