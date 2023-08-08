package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.addressform

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.entity.address.WarehouseDataModel
import com.tokopedia.logisticCommon.data.mapper.AddAddressMapper
import com.tokopedia.logisticCommon.data.repository.KeroRepository
import com.tokopedia.logisticCommon.data.response.DataAddAddress
import com.tokopedia.logisticCommon.data.response.DefaultAddressData
import com.tokopedia.logisticCommon.data.response.KeroEditAddressResponse
import com.tokopedia.logisticCommon.data.response.PinpointValidationResponse
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddressFormViewModel @Inject constructor(private val repo: KeroRepository) : ViewModel() {

    companion object {
        const val MIN_CHAR_PHONE_NUMBER = 9
        private const val MIN_CHAR_ADDRESS_LABEL = 3
        private const val MIN_CHAR_RECEIVER_NAME = 2
    }

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

    var saveDataModel: SaveAddressDataModel? = null

    val isHaveLatLong: Boolean
        get() = saveDataModel?.let { it.latitude.isNotEmpty() || it.longitude.isNotEmpty() }
            ?: false

    private var tempAddress1 = ""
    private var tempAddress2 = ""

    fun generateSaveDataModel(
        saveDataModel: SaveAddressDataModel?,
        defaultName: String,
        defaultPhone: String
    ): SaveAddressDataModel {
        return saveDataModel?.apply {
            if (this.receiverName.isEmpty()) this.receiverName = defaultName
            if (this.phone.isEmpty()) this.phone = defaultPhone
        } ?: SaveAddressDataModel(receiverName = defaultName, phone = defaultPhone)
    }

    fun getAddressDetail(
        addressId: String,
        sourceValue: String,
        draftAddressDataModel: SaveAddressDataModel?
    ) {
        if (draftAddressDataModel == null) {
            viewModelScope.launch {
                try {
                    val addressDetail = repo.getAddressDetail(addressId, sourceValue, needToTrack = true)
                    addressDetail.keroGetAddress.data.firstOrNull()?.let {
                        AddAddressMapper.mapAddressDetailToSaveAddressDataModel(it).apply {
                            saveDataModel = this

                            setCurrentLocation(
                                address = address1,
                                currentLat = latitude,
                                currentLong = longitude
                            )

                            _addressDetail.value = Success(this)
                        }
                    }
                } catch (e: Throwable) {
                    _addressDetail.value = Fail(e)
                }
            }
        } else {
            saveDraftAddress(draftAddressDataModel)
        }
    }

    fun getDefaultAddress(source: String) {
        viewModelScope.launch {
            try {
                val defaultAddress = repo.getDefaultAddress(source, needToTrack = true)
                _defaultAddress.value = Success(defaultAddress.response.data)
            } catch (e: Throwable) {
                _defaultAddress.value = Fail(e)
            }
        }
    }

    fun saveAddress(
        consentJson: String,
        sourceValue: String
    ) {
        saveDataModel?.let { model ->
            viewModelScope.launch {
                try {
                    val saveAddressData = repo.saveAddress(model, sourceValue, consentJson)

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

    fun saveEditAddress(model: SaveAddressDataModel, sourceValue: String) {
        viewModelScope.launch {
            try {
                val editAddressData = repo.editAddress(model, sourceValue)
                saveDataModel = editAddressData.keroEditAddress.data.toSaveAddressDataModel()
                _editAddress.value = Success(editAddressData.keroEditAddress.data)
            } catch (e: Throwable) {
                _editAddress.value = Fail(e)
            }
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

                _pinpointValidation.value =
                    Success(pinpointValidationResult.pinpointValidations.data)
            } catch (e: Exception) {
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
        isAnaPositive: String,
        isTokonow: Boolean
    ) {
        saveDataModel?.apply {
            this.receiverName = receiverName
            this.phone = phoneNo
            this.isTokonowRequest = true
            this.address1 = address1
            this.address1Notes = address1Notes
            this.addressName = addressName
            this.isAnaPositive = isAnaPositive
        }
    }

    fun validatePhoneNumber(
        phoneNumber: String?,
        onError: () -> Unit,
        onEmptyPhoneNumber: () -> Unit,
        onBelowMinCharacter: () -> Unit,
        onInvalidPhoneNumber: () -> Unit
    ) {
        phoneNumber?.run {
            if (phoneNumber.length < MIN_CHAR_PHONE_NUMBER) {
                if (phoneNumber.trim().isEmpty()) {
                    onEmptyPhoneNumber?.invoke()
                }
                onBelowMinCharacter.invoke()
                onError.invoke()
            } else if (!isPhoneNumberValid(phoneNumber)) {
                onInvalidPhoneNumber.invoke()
                onError.invoke()
            }
        } ?: onError.invoke()
    }

    fun validateReceiverName(
        receiverName: String?,
        onError: () -> Unit,
        onEmptyReceiverName: () -> Unit,
        onBelowMinCharacter: () -> Unit
    ) {
        receiverName?.run {
            if (receiverName.length < MIN_CHAR_RECEIVER_NAME) {
                if (receiverName.trim().isEmpty()) {
                    onEmptyReceiverName.invoke()
                }
                onBelowMinCharacter.invoke()
                onError.invoke()
            }
        } ?: onError.invoke()
    }

    fun validateAddress(
        address: String?,
        onError: () -> Unit,
        onEmptyAddress: () -> Unit,
        onBelowMinCharacter: () -> Unit,
        isErrorTextField: Boolean
    ) {
        address?.run {
            if (address.length < MIN_CHAR_ADDRESS_LABEL) {
                if (address.trim().isEmpty()) {
                    onEmptyAddress.invoke()
                }
                onBelowMinCharacter.invoke()
                onError.invoke()
            } else if (isErrorTextField) {
                onError.invoke()
            }
        } ?: onError.invoke()
    }

    fun validateLabel(
        label: String?,
        onError: () -> Unit,
        onEmptyLabel: () -> Unit,
        onBelowMinCharacter: () -> Unit
    ) {
        label?.run {
            if (label.length < MIN_CHAR_ADDRESS_LABEL) {
                if (label.trim().isEmpty()) {
                    onEmptyLabel.invoke()
                }
                onBelowMinCharacter.invoke()
                onError.invoke()
            }
        } ?: onError.invoke()
    }

    fun isDifferentLatLong(
        pinpointLat: String,
        pinpointLong: String
    ): Boolean {
        return pinpointLat != saveDataModel?.latitude || pinpointLong != saveDataModel?.longitude
    }

    fun isDifferentDistrictId(
        pinpointDistrictId: Long
    ): Boolean {
        return pinpointDistrictId != saveDataModel?.districtId
    }

    fun saveDraftAddress(saveAddressDataModel: SaveAddressDataModel) {
        this.saveDataModel = saveAddressDataModel
        _addressDetail.value = Success(saveAddressDataModel)
    }

    fun isDifferentLocation(
        address1: String,
        address2: String
    ): Boolean {
        return address1 != tempAddress1 || address2 != tempAddress2
    }

    private fun setCurrentLocation(
        address: String,
        currentLat: String,
        currentLong: String
    ) {
        tempAddress1 = address
        if (currentLat.isNotBlank() && currentLong.isNotBlank()) {
            tempAddress2 = "$currentLat,$currentLong"
        }
    }

    private fun KeroEditAddressResponse.Data.KeroEditAddress.KeroEditAddressSuccessResponse.toSaveAddressDataModel(): SaveAddressDataModel {
        val addressData = this.chosenAddressData
        val tokonowData = this.tokonow
        val warehouses = tokonowData.warehouses.map { WarehouseDataModel(warehouseId = it.warehouseId, serviceType = it.serviceType) }
        return SaveAddressDataModel(
            id = addressData.addressId,
            addressName = addressData.addressName,
            receiverName = addressData.receiverName,
            postalCode = addressData.postalCode,
            cityId = addressData.cityId.toLong(),
            districtId = addressData.districtId.toLong(),
            latitude = addressData.latitude,
            longitude = addressData.longitude,
            shopId = tokonowData.shopId,
            warehouseId = tokonowData.warehouseId,
            serviceType = tokonowData.serviceType,
            warehouses = warehouses
        )
    }
}
