package com.tokopedia.logisticaddaddress.domain.mapper

import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.get_district.GetDistrictDataUiModel
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.entity.response.Data
import javax.inject.Inject

class SaveAddressMapper @Inject constructor() {

    fun map(autoFillModel: Data, zipCodes: MutableList<String>?): SaveAddressDataModel {
        val saveAddressDataModel = SaveAddressDataModel()
        saveAddressDataModel.title = autoFillModel.title
        saveAddressDataModel.formattedAddress = autoFillModel.formattedAddress
        saveAddressDataModel.districtId = autoFillModel.districtId
        saveAddressDataModel.provinceId = autoFillModel.provinceId
        saveAddressDataModel.cityId = autoFillModel.cityId
        saveAddressDataModel.postalCode = autoFillModel.postalCode
        saveAddressDataModel.latitude = autoFillModel.latitude
        saveAddressDataModel.longitude = autoFillModel.longitude
        saveAddressDataModel.selectedDistrict = autoFillModel.formattedAddress
        if (zipCodes != null) {
            saveAddressDataModel.zipCodes = zipCodes
        }
        return saveAddressDataModel
    }

    fun map(getDistrictDataUiModel: GetDistrictDataUiModel, zipCodes: MutableList<String>?): SaveAddressDataModel {
        val saveAddressDataModel = SaveAddressDataModel()
        saveAddressDataModel.title = getDistrictDataUiModel.title
        saveAddressDataModel.formattedAddress = getDistrictDataUiModel.formattedAddress
        saveAddressDataModel.districtId = getDistrictDataUiModel.districtId
        saveAddressDataModel.provinceId = getDistrictDataUiModel.provinceId
        saveAddressDataModel.cityId = getDistrictDataUiModel.cityId
        saveAddressDataModel.postalCode = getDistrictDataUiModel.postalCode
        saveAddressDataModel.latitude = getDistrictDataUiModel.latitude
        saveAddressDataModel.longitude = getDistrictDataUiModel.longitude
        saveAddressDataModel.selectedDistrict = getDistrictDataUiModel.formattedAddress
        if (zipCodes != null) {
            saveAddressDataModel.zipCodes = zipCodes
        }
        return saveAddressDataModel
    }

}