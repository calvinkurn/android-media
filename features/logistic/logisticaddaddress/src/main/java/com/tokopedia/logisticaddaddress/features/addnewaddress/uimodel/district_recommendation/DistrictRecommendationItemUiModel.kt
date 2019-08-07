package com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.district_recommendation

/**
 * Created by fwidjaja on 2019-05-31.
 */
data class DistrictRecommendationItemUiModel (
        var districtId: Int = 0,
        var districtName: String = "",
        var cityId: Int = 0,
        var cityName: String = "",
        var provinceId: Int = 0,
        var provinceName: String = "",
        var zipCodes: List<String> = emptyList())