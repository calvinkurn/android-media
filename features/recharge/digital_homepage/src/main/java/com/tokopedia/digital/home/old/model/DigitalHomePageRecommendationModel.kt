package com.tokopedia.digital.home.old.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.common_digital.common.presentation.model.RechargeFavoriteRecommendationList
import com.tokopedia.digital.home.old.presentation.adapter.DigitalHomePageTypeFactory

class DigitalHomePageRecommendationModel(@SerializedName("rechargeFavoriteRecommendationList")
                                 @Expose
                                 val data: RechargeFavoriteRecommendationList = RechargeFavoriteRecommendationList()) : DigitalHomePageItemModel() {

    override fun type(typeFactory: DigitalHomePageTypeFactory): Int {
        return typeFactory.type(this)
    }
}