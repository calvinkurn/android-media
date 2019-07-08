package com.tokopedia.logisticaddaddress.domain.mapper

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.logisticaddaddress.domain.model.district_recommendation.DistrictItem
import com.tokopedia.logisticaddaddress.domain.model.district_recommendation.DistrictRecommendationResponse
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.district_recommendation.DistrictRecommendationItemUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.district_recommendation.DistrictRecommendationResponseUiModel
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-31.
 */
class DistrictRecommendationMapper @Inject constructor() {

    fun map(response: GraphqlResponse?): DistrictRecommendationResponseUiModel {
        var listDistrict = mutableListOf<DistrictRecommendationItemUiModel>()
        val responseDistrictRecommendation: DistrictRecommendationResponse? = response?.getData(DistrictRecommendationResponse::class.java)
        responseDistrictRecommendation?.keroDistrictRecommendation?.let { keroDistrictRecommendation ->
            keroDistrictRecommendation.district.forEach {
                mapDistrictItem(it).let(listDistrict::add)
            }
        }
        return DistrictRecommendationResponseUiModel(listDistrict)
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