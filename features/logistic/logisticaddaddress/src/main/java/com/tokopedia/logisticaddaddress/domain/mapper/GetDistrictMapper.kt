package com.tokopedia.logisticaddaddress.domain.mapper

import com.tokopedia.logisticaddaddress.domain.model.get_district.Data
import com.tokopedia.logisticaddaddress.domain.model.get_district.GetDistrictResponse
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.get_district.GetDistrictDataUiModel
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-16.
 */
class GetDistrictMapper @Inject constructor() {

    fun map(response: GetDistrictResponse): GetDistrictDataUiModel {
        return mapData(response.keroPlacesGetDistrict.data ?: Data())
    }

    private fun mapData(data: Data): GetDistrictDataUiModel {
        return GetDistrictDataUiModel(
                title = data.title,
                formattedAddress = data.formattedAddress,
                latitude = data.latitude,
                longitude = data.longitude,
                districtId = data.districtId,
                postalCode = data.postalCode,
                cityId = data.cityId,
                provinceId = data.provinceId
        )
    }
}