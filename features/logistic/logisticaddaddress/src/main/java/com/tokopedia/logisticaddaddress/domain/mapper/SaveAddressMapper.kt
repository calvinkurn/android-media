package com.tokopedia.logisticaddaddress.domain.mapper

import com.tokopedia.logisticdata.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticdata.data.entity.response.Data
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

}