package com.tokopedia.digital.home.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.common_digital.common.presentation.model.RechargeFavoriteRecommendationList
import com.tokopedia.digital.home.presentation.adapter.DigitalHomePageAdapterFactory

class DigitalHomePageRecommendationModel(@SerializedName("rechargeFavoriteRecommendationList")
                                 @Expose
                                 val data: RechargeFavoriteRecommendationList = RechargeFavoriteRecommendationList()) : DigitalHomePageItemModel() {

    override fun visitableId(): Int {
        return RECOMMENDATION_SECTION_ID
    }

    override fun equalsWith(b: Any?): Boolean {
        return if (b is DigitalHomePageRecommendationModel) {
            this.data.recommendationItemEntityList == b.data.recommendationItemEntityList
        } else false
    }

    override fun type(typeFactory: DigitalHomePageAdapterFactory): Int {
        return typeFactory.type(this)
    }

    companion object {
        const val RECOMMENDATION_SECTION_ID = -7
    }
}