package com.tokopedia.logisticaddaddress.domain.mapper

import com.tokopedia.logisticaddaddress.domain.model.get_district.GetDistrictResponse
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.get_district.GetDistrictDataUiModel
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-16.
 */
class GetDistrictMapper @Inject constructor() {

    fun map(response: GetDistrictResponse): GetDistrictDataUiModel {
        val msg = response.keroPlacesGetDistrict.messageError.firstOrNull()
        val errorCode = response.keroPlacesGetDistrict.errorCode
        with(response.keroPlacesGetDistrict.data) {
            return GetDistrictDataUiModel(
                    title = this.title,
                    formattedAddress = this.formattedAddress,
                    latitude = this.latitude,
                    longitude = this.longitude,
                    districtId = this.districtId,
                    postalCode = this.postalCode,
                    cityId = this.cityId,
                    provinceId = this.provinceId,
                    errMessage = msg,
                    errorCode = errorCode
            )
        }
    }

}