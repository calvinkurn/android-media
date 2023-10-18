package com.tokopedia.logisticaddaddress.domain.mapper

import com.tokopedia.logisticCommon.data.response.GetDistrictResponse
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.uimodel.GetDistrictDataUiModel
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
                districtName = this.districtName,
                provinceName = this.provinceName,
                cityName = this.cityName,
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
