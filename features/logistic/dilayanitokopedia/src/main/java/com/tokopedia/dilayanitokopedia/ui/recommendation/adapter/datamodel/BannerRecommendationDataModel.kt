package com.tokopedia.dilayanitokopedia.ui.recommendation.adapter.datamodel

import android.os.Bundle
import com.tokopedia.dilayanitokopedia.ui.recommendation.adapter.factory.HomeRecommendationTypeFactory
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

        if (id != other.id) {
            return false
        } else if (name != other.name) {
            return false
        } else if (imageUrl != other.imageUrl) {
            return false
        } else if (url != other.url) {
            return false
        } else if (applink != other.applink) {
            return false
        } else if (buAttribution != other.buAttribution) {
            return false
        } else if (creativeName != other.creativeName) {
            return false
        } else if (target != other.target) {
            return false
        } else if (position != other.position) {
            return false
        } else if (galaxyAttribution != other.galaxyAttribution) {
            return false
        } else if (affinityLabel != other.affinityLabel) {
            return false
        } else if (shopId != other.shopId) {
            return false
        } else if (categoryPersona != other.categoryPersona) return false

        return true
    }

    override fun hashCode(): Int {
        val mainId = 31
        var result = id
        result = mainId * result + name.hashCode()
        result = mainId * result + imageUrl.hashCode()
        result = mainId * result + url.hashCode()
        result = mainId * result + applink.hashCode()
        result = mainId * result + buAttribution.hashCode()
        result = mainId * result + creativeName.hashCode()
        result = mainId * result + target.hashCode()
        result = mainId * result + position
        result = mainId * result + galaxyAttribution.hashCode()
        result = mainId * result + affinityLabel.hashCode()
        result = mainId * result + shopId.hashCode()
        result = mainId * result + categoryPersona.hashCode()
        return result
    }
}
