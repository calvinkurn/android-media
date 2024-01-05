package com.tokopedia.logisticaddaddress.domain.mapper

import com.tokopedia.localizationchooseaddress.domain.model.DistrictRecommendationAddressModel
import com.tokopedia.logisticCommon.data.entity.address.DistrictRecommendationAddress
import com.tokopedia.logisticCommon.data.entity.response.Data
import com.tokopedia.logisticaddaddress.domain.model.Address
import javax.inject.Inject

class AddressMapper @Inject constructor() {
    fun convertAddress(address: Address): DistrictRecommendationAddress {
        val districtAddress = DistrictRecommendationAddress()
        districtAddress.cityId = address.cityId
        districtAddress.cityName = address.cityName
        districtAddress.districtId = address.districtId
        districtAddress.districtName = address.districtName
        districtAddress.provinceId = address.provinceId
        districtAddress.provinceName = address.provinceName
        districtAddress.zipCodes = ArrayList(address.zipCodes)
        return districtAddress
    }

    fun convertAutofillResponse(data: Data): DistrictRecommendationAddress {
        val districtAddress = DistrictRecommendationAddress()
        val arrayPostalCode = ArrayList<String>()
        arrayPostalCode.add(data.postalCode)
        districtAddress.cityId = data.cityId

        // note : city name is not provided by backend
        districtAddress.cityName = ""
        districtAddress.districtId = data.districtId
        districtAddress.districtName = data.districtName
        districtAddress.provinceId = data.provinceId

        // note : province name is not provided by backend
        districtAddress.provinceName = ""
        districtAddress.zipCodes = arrayPostalCode
        return districtAddress
    }

    fun convertToAddressLocalizationModel(address: Address): DistrictRecommendationAddressModel {
        val districtAddressModel = DistrictRecommendationAddressModel()
        districtAddressModel.cityId = address.cityId
        districtAddressModel.cityName = address.cityName
        districtAddressModel.districtId = address.districtId
        districtAddressModel.districtName = address.districtName
        districtAddressModel.provinceId = address.provinceId
        districtAddressModel.provinceName = address.provinceName
        districtAddressModel.provinceCode = ArrayList(address.zipCodes)
        return districtAddressModel
    }
}
