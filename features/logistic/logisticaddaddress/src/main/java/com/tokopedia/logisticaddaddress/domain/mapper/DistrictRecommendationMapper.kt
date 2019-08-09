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
            addresses = response.keroDistrictRecommendation.district
                    .map { mapDistrictToAddress(it) }
                    as ArrayList<Address>
            isNextAvailable = response.keroDistrictRecommendation.nextAvailable
        }
    }

    private fun mapDistrictToAddress(district: DistrictItem): Address = Address().apply {
        districtId = district.districtId
        districtName = district.districtName
        cityId = district.cityId
        cityName = district.cityName
        provinceId = district.provinceId
        provinceName = district.provinceName
        if (district.zipCode.isNotEmpty()) {
            zipCodes = district.zipCode as ArrayList<String>
        }
    }

}