package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.addressform

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.logisticCommon.data.constant.ManageAddressSource
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.mapper.AddAddressMapper
import com.tokopedia.logisticCommon.data.repository.KeroRepository
import com.tokopedia.logisticCommon.data.response.DataAddAddress
import com.tokopedia.logisticCommon.data.response.DefaultAddressData
import com.tokopedia.logisticCommon.data.response.KeroDistrictRecommendation
import com.tokopedia.logisticCommon.data.response.KeroEditAddressResponse
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

    private val _editAddress =
        MutableLiveData<Result<KeroEditAddressResponse.Data.KeroEditAddress.KeroEditAddressSuccessResponse>>()
    val editAddress: LiveData<Result<KeroEditAddressResponse.Data.KeroEditAddress.KeroEditAddressSuccessResponse>>
        get() = _editAddress

    private val _defaultAddress = MutableLiveData<Result<DefaultAddressData>>()
    val defaultAddress: LiveData<Result<DefaultAddressData>>
        get() = _defaultAddress

    private val _addressDetail = MutableLiveData<Result<SaveAddressDataModel>>()
    val addressDetail: LiveData<Result<SaveAddressDataModel>>
        get() = _addressDetail

    private val _pinpointValidation =
        MutableLiveData<Result<PinpointValidationResponse.PinpointValidations.PinpointValidationResponseData>>()
    val pinpointValidation: LiveData<Result<PinpointValidationResponse.PinpointValidations.PinpointValidationResponseData>>
        get() = _pinpointValidation

    var source: String = ""
    var isGmsAvailable: Boolean = true
    val isTokonow: Boolean
        get() = source == ManageAddressSource.TOKONOW.source

    var saveDataModel: SaveAddressDataModel? = null
    var isPositiveFlow: Boolean = true
    var validated: Boolean = true
    var isEdit: Boolean = false
    var addressId: String = ""

    val isHaveLatLong: Boolean
        get() = saveDataModel?.let { it.latitude.isNotEmpty() || it.longitude.isNotEmpty() }
            ?: false

    fun setDataFromArguments(
        isEdit: Boolean,
        saveDataModel: SaveAddressDataModel?,
        isPositiveFlow: Boolean,
        addressId: String,
        source: String,
        onViewEditAddressPageNew: () -> Unit
    ) {
        this.isEdit = isEdit
        if (!isEdit) {
            this.saveDataModel = saveDataModel
            this.isPositiveFlow = isPositiveFlow
        } else {
            onViewEditAddressPageNew.invoke()
            this.addressId = addressId
        }
        this.source = source
    }

    fun getDistrictDetail() {
        viewModelScope.launch {
            try {
                val districtDetail = repo.getZipCode(saveDataModel?.districtId.toString())
                _districtDetail.value = Success(districtDetail.keroDistrictDetails)
            } catch (e: Throwable) {
                _districtDetail.value = Fail(e)
            }
        }
    }

    fun getAddressDetail() {
        viewModelScope.launch {
            try {
                val addressDetail = repo.getAddressDetail(addressId, getSourceValue())
                addressDetail.keroGetAddress.data.firstOrNull()?.let {
                    AddAddressMapper.mapAddressDetailToSaveAddressDataModel(it).apply {
                        saveDataModel = this
                        isPositiveFlow = hasPinpoint().orFalse()
                        _addressDetail.value = Success(this)
                    }
                }
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

    fun saveAddress() {
        saveDataModel?.let { model ->
            viewModelScope.launch {
                try {
                    val saveAddressData = repo.saveAddress(model, getSourceValue())

                    saveAddressData.keroAddAddress.data.takeIf {
                        it.isSuccess == 1
                    }?.apply {
                        model.updateDataWarehouses(this)
                        _saveAddress.value = Success(this)
                    }
                } catch (e: Throwable) {
                    _saveAddress.value = Fail(e)
                }
            }
        }
    }

    private fun SaveAddressDataModel.updateDataWarehouses(data: DataAddAddress) {
        id = data.addrId
        warehouseId = data.tokonow.warehouseId
        shopId = data.tokonow.shopId
        warehouses =
            AddAddressMapper.mapWarehouses(data.tokonow.warehouses)
        serviceType = data.tokonow.serviceType
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
                val pinpointValidationResult = repo.pinpointValidation(
                    model.districtId.toInt(),
                    model.latitude,
                    model.longitude,
                    model.postalCode
                )
                if (pinpointValidationResult.pinpointValidations.data.result) {
                    saveDataModel?.let { addressData -> saveEditAddress(addressData) }
                }
                _pinpointValidation.value =
                    Success(pinpointValidationResult.pinpointValidations.data)
            } catch (e: Throwable) {
                _pinpointValidation.value = Fail(e)
            }
        }
    }

    fun clearLatLong() {
        saveDataModel?.apply {
            latitude = ""
            longitude = ""
        }
    }

    fun removeSpecialChars(s: String): String {
        return s.replace("[^A-Za-z0-9 ]".toRegex(), "").replace(" ", "")
    }

    fun isPhoneNumberValid(phone: String): Boolean {
        val phoneRule = Regex("^(^62\\d{7,13}|^0\\d{8,14})$")
        return phoneRule.matches(phone)
    }

    fun updateDataSaveModel(
        receiverName: String,
        phoneNo: String,
        address1: String,
        address1Notes: String,
        addressName: String,
        isAnaPositive: String
    ) {
        saveDataModel?.apply {
            this.receiverName = receiverName
            this.phone = phoneNo
            this.isTokonowRequest = isTokonow
            this.address1 = address1
            this.address1Notes = address1Notes
            this.addressName = addressName
            this.isAnaPositive = isAnaPositive
        }
    }
}
