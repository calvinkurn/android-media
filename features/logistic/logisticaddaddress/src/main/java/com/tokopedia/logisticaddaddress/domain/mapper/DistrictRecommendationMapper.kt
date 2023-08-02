package com.tokopedia.logisticaddaddress.domain.mapper

import com.tokopedia.logisticaddaddress.domain.model.Address
import com.tokopedia.logisticaddaddress.domain.model.AddressResponse
import com.tokopedia.logisticaddaddress.domain.model.district_recommendation.DistrictItem
import com.tokopedia.logisticaddaddress.domain.model.district_recommendation.DistrictRecommendationResponse
import java.util.ArrayList
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-31.
 */
class DistrictRecommendationMapper @Inject constructor() {

    fun transform(response: DistrictRecommendationResponse): AddressResponse {
        return AddressResponse().apply {
            addresses = response.keroDistrictRecommendation.district.mapToAddressResponse()
            isNextAvailable = response.keroDistrictRecommendation.nextAvailable
        }
    }

    private fun List<DistrictItem>.mapToAddressResponse(): ArrayList<Address> {
        return this.map {
            Address().apply {
                districtId = it.districtId
                districtName = it.districtName
                cityId = it.cityId
                cityName = it.cityName
                provinceId = it.provinceId
                provinceName = it.provinceName
                zipCodes = it.zipCode
            }
        } as ArrayList<Address>
    }
}
