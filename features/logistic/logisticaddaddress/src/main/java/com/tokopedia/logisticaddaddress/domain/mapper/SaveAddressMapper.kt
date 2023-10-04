package com.tokopedia.logisticaddaddress.domain.mapper

import com.tokopedia.kotlin.extensions.view.toDoubleOrZero
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.entity.response.Data
import com.tokopedia.logisticaddaddress.domain.model.Address
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.uimodel.GetDistrictDataUiModel
import com.tokopedia.logisticaddaddress.features.pinpoint.pinpointnew.uimodel.PinpointUiModel

object SaveAddressMapper {

    fun map(
        autoFillModel: Data,
        zipCodes: MutableList<String>?,
        existingModel: SaveAddressDataModel? = null
    ): SaveAddressDataModel {
        val saveAddressDataModel = existingModel ?: SaveAddressDataModel()
        saveAddressDataModel.title = autoFillModel.title
        saveAddressDataModel.formattedAddress =
            "${autoFillModel.districtName}, ${autoFillModel.cityName}, ${autoFillModel.provinceName}"
        saveAddressDataModel.districtId = autoFillModel.districtId
        saveAddressDataModel.provinceId = autoFillModel.provinceId
        saveAddressDataModel.cityId = autoFillModel.cityId
        saveAddressDataModel.districtName = autoFillModel.districtName
        saveAddressDataModel.provinceName = autoFillModel.provinceName
        saveAddressDataModel.cityName = autoFillModel.cityName
        saveAddressDataModel.postalCode = autoFillModel.postalCode
        saveAddressDataModel.latitude = autoFillModel.latitude
        saveAddressDataModel.longitude = autoFillModel.longitude
        saveAddressDataModel.address2 = "${autoFillModel.latitude},${autoFillModel.longitude}"
        saveAddressDataModel.selectedDistrict = autoFillModel.formattedAddress
        if (zipCodes != null) {
            saveAddressDataModel.zipCodes = zipCodes
        }
        return saveAddressDataModel
    }

    fun PinpointUiModel.map(
        autoFillModel: Data,
        zipCodes: MutableList<String>?
    ): PinpointUiModel {
        this.title = autoFillModel.title
        this.districtId = autoFillModel.districtId
        this.provinceId = autoFillModel.provinceId
        this.cityId = autoFillModel.cityId
        this.districtName = autoFillModel.districtName
        this.provinceName = autoFillModel.provinceName
        this.cityName = autoFillModel.cityName
        this.postalCode = autoFillModel.postalCode
        this.lat = autoFillModel.latitude.toDoubleOrZero()
        this.long = autoFillModel.longitude.toDoubleOrZero()
        if (zipCodes != null) {
            this.postalCodeList = zipCodes
        }
        return this
    }

    fun map(
        getDistrictDataUiModel: GetDistrictDataUiModel,
        zipCodes: MutableList<String>?,
        existingModel: SaveAddressDataModel? = null
    ): SaveAddressDataModel {
        val saveAddressDataModel = existingModel ?: SaveAddressDataModel()
        saveAddressDataModel.title = getDistrictDataUiModel.title
        saveAddressDataModel.formattedAddress =
            "${getDistrictDataUiModel.districtName}, ${getDistrictDataUiModel.cityName}, ${getDistrictDataUiModel.provinceName}"
        saveAddressDataModel.districtId = getDistrictDataUiModel.districtId
        saveAddressDataModel.provinceId = getDistrictDataUiModel.provinceId
        saveAddressDataModel.cityId = getDistrictDataUiModel.cityId
        saveAddressDataModel.districtName = getDistrictDataUiModel.districtName
        saveAddressDataModel.provinceName = getDistrictDataUiModel.provinceName
        saveAddressDataModel.cityName = getDistrictDataUiModel.cityName
        saveAddressDataModel.postalCode = getDistrictDataUiModel.postalCode
        saveAddressDataModel.latitude = getDistrictDataUiModel.latitude
        saveAddressDataModel.longitude = getDistrictDataUiModel.longitude
        saveAddressDataModel.selectedDistrict = getDistrictDataUiModel.formattedAddress
        if (zipCodes != null) {
            saveAddressDataModel.zipCodes = zipCodes
        }
        return saveAddressDataModel
    }

    fun PinpointUiModel.map(
        getDistrictDataUiModel: GetDistrictDataUiModel,
        zipCodes: MutableList<String>?
    ): PinpointUiModel {
        this.title = getDistrictDataUiModel.title
        this.districtId = getDistrictDataUiModel.districtId
        this.provinceId = getDistrictDataUiModel.provinceId
        this.cityId = getDistrictDataUiModel.cityId
        this.districtName = getDistrictDataUiModel.districtName
        this.provinceName = getDistrictDataUiModel.provinceName
        this.cityName = getDistrictDataUiModel.cityName
        this.postalCode = getDistrictDataUiModel.postalCode
        this.lat = getDistrictDataUiModel.latitude.toDoubleOrZero()
        this.long = getDistrictDataUiModel.longitude.toDoubleOrZero()
        if (zipCodes != null) {
            this.postalCodeList = zipCodes
        }
        return this
    }

    fun mapAddressModeltoSaveAddressDataModel(
        address: Address,
        postalCode: String,
        saveAddressDataModel: SaveAddressDataModel?
    ): SaveAddressDataModel {
        val data = saveAddressDataModel ?: SaveAddressDataModel()
        return data.apply {
            this.districtId = address.districtId
            this.selectedDistrict =
                "${address.provinceName}, ${address.cityName}, ${address.districtName}"
            this.cityId = address.cityId
            this.provinceId = address.provinceId
            this.districtId = address.districtId
            this.zipCodes = address.zipCodes.toList()
            this.postalCode = postalCode
            this.formattedAddress =
                "${address.districtName}, ${address.cityName}, ${address.provinceName}"
        }
    }
}
