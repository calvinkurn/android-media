package com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou

import android.os.Bundle
import com.tokopedia.dilayanitokopedia.home.presentation.factory.HomeRecommendationTypeFactory
import com.tokopedia.dilayanitokopedia.home.presentation.uimodel.HomeRecommendationVisitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.smart_recycler_helper.SmartVisitable

data class BannerRecommendationDataModel(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val url: String,
    val applink: String,
    val buAttribution: String,
    val creativeName: String,
    val target: String,
    val position: Int,
    val galaxyAttribution: String,
    val affinityLabel: String,
    val shopId: String,
    val categoryPersona: String,
    val tabName: String
) : ImpressHolder(), HomeRecommendationVisitable {

    override fun getChangePayloadFrom(b: Any?): Bundle? {
        return Bundle()
    }

    override fun equalsDataModel(dataModel: SmartVisitable<*>): Boolean {
        return dataModel == this
    }

    override fun type(typeFactory: HomeRecommendationTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun getUniqueIdentity(): Any {
        return id.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BannerRecommendationDataModel

        if (id != other.id) return false
        if (name != other.name) return false
        if (imageUrl != other.imageUrl) return false
        if (url != other.url) return false
        if (applink != other.applink) return false
        if (buAttribution != other.buAttribution) return false
        if (creativeName != other.creativeName) return false
        if (target != other.target) return false
        if (position != other.position) return false
        if (galaxyAttribution != other.galaxyAttribution) return false
        if (affinityLabel != other.affinityLabel) return false
        if (shopId != other.shopId) return false
        if (categoryPersona != other.categoryPersona) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + imageUrl.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + applink.hashCode()
        result = 31 * result + buAttribution.hashCode()
        result = 31 * result + creativeName.hashCode()
        result = 31 * result + target.hashCode()
        result = 31 * result + position
        result = 31 * result + galaxyAttribution.hashCode()
        result = 31 * result + affinityLabel.hashCode()
        result = 31 * result + shopId.hashCode()
        result = 31 * result + categoryPersona.hashCode()
        return result
    }
}
