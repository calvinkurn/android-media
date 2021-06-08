package com.tokopedia.buyerorder.detail.data.recommendation.recommendationMPPojo2

import com.google.gson.annotations.SerializedName

data class RecommendationDigiPersoResponse(
        @field:SerializedName("digiPersoGetPersonalizedItems")
        val personalizedItems: PersonalizedItems?
)

data class PersonalizedItems(
        @field:SerializedName("applink")
        val appLink: String?,

        @field:SerializedName("bannerAppLink")
        val bannerAppLink: String?,

        @field:SerializedName("bannerWebLink")
        val bannerWebLink: String?,

        @field:SerializedName("items")
        val recommendationItems: List<RecommendationItem>?,

        @field:SerializedName("mediaURL")
        val mediaURL: String?,

        @field:SerializedName("option1")
        val option1: String?,

        @field:SerializedName("option2")
        val option2: String?,

        @field:SerializedName("option3")
        val option3: String?,

        @field:SerializedName("textLink")
        val textLink: String?,

        @field:SerializedName("title")
        val title: String?,

        @field:SerializedName("tracking")
        val tracking: List<Any>?,

        @field:SerializedName("webLink")
        val webLink: String?
)

data class RecommendationItem(

        @field:SerializedName("appLink")
        val appLink: String?,

        @field:SerializedName("backgroundColor")
        val backgroundColor: String?,

        @field:SerializedName("id")
        val id: Int = 0,

        @field:SerializedName("label1")
        val label1: String?,

        @field:SerializedName("label1Mode")
        val label1Mode: String?,

        @field:SerializedName("label2")
        val label2: String?,

        @field:SerializedName("label3")
        val label3: String?,

        @field:SerializedName("mediaURL")
        val mediaURL: String?,

        @field:SerializedName("mediaUrlType")
        val mediaUrlType: String?,

        @field:SerializedName("subtitle")
        val subtitle: String?,

        @field:SerializedName("subtitleMode")
        val subtitleMode: String?,

        @field:SerializedName("title")
        val title: String?,

        @field:SerializedName("trackingData")
        val trackingData: TrackingData?,

        @field:SerializedName("webLink")
        val webLink: String?
)

data class TrackingData(
        @field:SerializedName("__typename")
        val __typename: String?,

        @field:SerializedName("businessUnit")
        val businessUnit: String?,

        @field:SerializedName("categoryID")
        val categoryID: String?,

        @field:SerializedName("categoryName")
        val categoryName: String?,

        @field:SerializedName("itemLabel")
        val itemLabel: String?,

        @field:SerializedName("itemType")
        val itemType: String?,

        @field:SerializedName("operatorID")
        val operatorID: String?,

        @field:SerializedName("productID")
        val productID: String?
)