package com.tokopedia.logisticaddaddress.domain.mapper

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.logisticaddaddress.domain.model.get_district.Data
import com.tokopedia.logisticaddaddress.domain.model.get_district.GetDistrictResponse
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.get_district.GetDistrictDataUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.get_district.GetDistrictResponseUiModel
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-16.
 */
open class GetDistrictMapper @Inject constructor() {

    fun map(response: GraphqlResponse?) : GetDistrictResponseUiModel {
        var dataUiModel = GetDistrictDataUiModel()
        val responseGetDistrict: GetDistrictResponse? = response?.getData(GetDistrictResponse::class.java)
        responseGetDistrict.let { responseGetDistrict ->
            responseGetDistrict?.keroPlacesGetDistrict.let {
                dataUiModel = it?.data?.let { it1 -> mapData(it1) }!!
            }
        }
        return GetDistrictResponseUiModel(dataUiModel)
    }

    private fun mapData(data: Data) : GetDistrictDataUiModel {
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