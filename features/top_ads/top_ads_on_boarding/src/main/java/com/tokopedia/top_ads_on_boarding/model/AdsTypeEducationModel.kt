package com.tokopedia.top_ads_on_boarding.model

data class AdsTypeEducationModel(
    var title: String = "",
    var subTitle: String = "",
    var description: String = "",
    var pointsList: List<AdsTypeEducation>
) {
    data class Points(
        var points_Title: String = "",
        var points_Description: String = "",
        var points_Icon: Int? = null
    ) : AdsTypeEducation()

    data class Header(
        var title: String = "",
        var subTitle: String = "",
        var description: String = "",
        var headerImage: Pair<String,String>? = null
    ) : AdsTypeEducation()
}

open class AdsTypeEducation
