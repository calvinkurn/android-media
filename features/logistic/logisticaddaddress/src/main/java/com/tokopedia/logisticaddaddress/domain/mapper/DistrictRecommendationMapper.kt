package com.tokopedia.logisticaddaddress.domain.mapper

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.logisticaddaddress.domain.model.Address
import com.tokopedia.logisticaddaddress.domain.model.AddressResponse
import com.tokopedia.logisticaddaddress.domain.model.district_recommendation.DistrictItem
import com.tokopedia.logisticaddaddress.domain.model.district_recommendation.DistrictRecommendationResponse
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.district_recommendation.DistrictRecommendationItemUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.district_recommendation.DistrictRecommendationResponseUiModel
import java.util.ArrayList
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-31.
 */
class DistrictRecommendationMapper @Inject constructor() {

    fun map(response: GraphqlResponse?): DistrictRecommendationResponseUiModel {
        var listDistrict = mutableListOf<DistrictRecommendationItemUiModel>()
        val responseDistrictRecommendation: DistrictRecommendationResponse? = response?.getData(DistrictRecommendationResponse::class.java)
        responseDistrictRecommendation?.keroDistrictRecommendation.let { keroDistrictRecommendation ->
            keroDistrictRecommendation?.district?.forEach {
                mapDistrictItem(it).let(listDistrict::add)
            }
        }
        return DistrictRecommendationResponseUiModel(listDistrict)
    }

    fun transform(response: DistrictRecommendationResponse): DistrictRecommendationResponseUiModel {
        return DistrictRecommendationResponseUiModel().apply {
            listDistrict = response.keroDistrictRecommendation.district.map { mapDistrictItem(it) }
            hasNext = response.keroDistrictRecommendation.nextAvailable
        }
    }

    fun transformViewModel(response: DistrictRecommendationResponse): AddressResponse {
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

    private fun mapDistrictItem(district: DistrictItem): DistrictRecommendationItemUiModel {
        return DistrictRecommendationItemUiModel(
                districtId = district.districtId,
                cityId = district.cityId,
                cityName = district.cityName,
                provinceId = district.provinceId,
                provinceName = district.provinceName,
                zipCodes = district.zipCode,
                districtName = district.districtName
        )
    }
}