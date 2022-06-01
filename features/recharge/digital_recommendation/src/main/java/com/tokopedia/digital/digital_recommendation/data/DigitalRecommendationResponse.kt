package com.tokopedia.digital.digital_recommendation.data

import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 20/09/2021
 */
data class DigitalRecommendationResponse(
        @field:SerializedName("digiPersoGetPersonalizedItems")
        val personalizedItems: PersonalizedItems = PersonalizedItems()
)

data class PersonalizedItems(
        @field:SerializedName("applink")
        val appLink: String = "",

        @field:SerializedName("bannerAppLink")
        val bannerAppLink: String = "",

        @field:SerializedName("bannerWebLink")
        val bannerWebLink: String = "",

        @field:SerializedName("items")
        val recommendationItems: List<RecommendationItem> = emptyList(),

        @field:SerializedName("mediaURL")
        val mediaURL: String = "",

        @field:SerializedName("mediaURLType")
        val mediaUrlType: String = "",

        @field:SerializedName("option1")
        val option1: String = "",

        @field:SerializedName("option2")
        val option2: String = "",

        @field:SerializedName("option3")
        val option3: String = "",

        @field:SerializedName("textLink")
        val textLink: String = "",

        @field:SerializedName("title")
        val title: String = "",

        @field:SerializedName("tracking")
        val tracking: List<Any> = emptyList(),

        @field:SerializedName("trackingData")
        val trackingData: UserTrackingData = UserTrackingData(),

        @field:SerializedName("webLink")
        val webLink: String = ""
)

data class RecommendationItem(

        @field:SerializedName("appLink")
        val appLink: String = "",

        @field:SerializedName("backgroundColor")
        val backgroundColor: String = "",

        @field:SerializedName("campaignLabelText")
        val campaignLabelText: String = "",

        @field:SerializedName("campaignLabelTextColor")
        val campaignLabelTextColor: String = "",

        @field:SerializedName("campaignLabelBackgroundURL")
        val campaignLabelBackgroundUrl: String = "",

        @field:SerializedName("productInfo1")
        val productInfo1: ProductInfo = ProductInfo(),

        @field:SerializedName("productInfo2")
        val productInfo2: ProductInfo = ProductInfo(),

        @field:SerializedName("ratingType")
        val ratingType: String = "",

        @field:SerializedName("rating")
        val rating: Double = 0.0,

        @field:SerializedName("review")
        val review: String = "",

        @field:SerializedName("soldPercentageValue")
        val soldPercentageValue: Int = 0,

        @field:SerializedName("soldPercentageLabel")
        val soldPercentageLabel: String = "",

        @field:SerializedName("soldPercentageLabelColor")
        val soldPercentageLabelColor: String = "",

        @field:SerializedName("showSoldPercentage")
        val showSoldPercentage: Boolean = false,

        @field:SerializedName("slashedPrice")
        val slashedPrice: String = "",

        @field:SerializedName("discount")
        val discount: String = "",

        @field:SerializedName("cashback")
        val cashback: String = "",

        @field:SerializedName("specialDiscount")
        val specialDiscount: String = "",

        @field:SerializedName("price")
        val price: String = "",

        @field:SerializedName("pricePrefix")
        val pricePrefix: String = "",

        @field:SerializedName("priceSuffix")
        val priceSuffix: String = "",

        @field:SerializedName("specialInfoText")
        val specialInfoText: String = "",

        @field:SerializedName("specialInfoColor")
        val specialInfoColor: String = "",

        @field:SerializedName("id")
        val id: String = "",

        @field:SerializedName("label1")
        val label1: String = "",

        @field:SerializedName("label1Mode")
        val label1Mode: String = "",

        @field:SerializedName("label2")
        val label2: String = "",

        @field:SerializedName("label3")
        val label3: String = "",

        @field:SerializedName("mediaURL")
        val mediaURL: String = "",

        @field:SerializedName("mediaUrlType")
        val mediaUrlType: String = "",

        @field:SerializedName("mediaURLTitle")
        val mediaUrlTitle: String = "",

        @field:SerializedName("iconURL")
        val iconURL: String = "",

        @field:SerializedName("subtitle")
        val subtitle: String = "",

        @field:SerializedName("subtitleMode")
        val subtitleMode: String = "",

        @field:SerializedName("title")
        val title: String = "",

        @field:SerializedName("trackingData")
        val trackingData: TrackingData = TrackingData(),

        @field:SerializedName("webLink")
        val webLink: String = "",

        @field:SerializedName("textLink")
        val textLink: String = "",

        @field:SerializedName("textLinkColor")
        val textLinkColor: String = ""
)

data class TrackingData(
        @field:SerializedName("__typename")
        val __typename: String = "",

        @field:SerializedName("pricePlain")
        val pricePlain: String = "",

        @field:SerializedName("businessUnit")
        val businessUnit: String = "",

        @field:SerializedName("categoryID")
        val categoryID: String = "",

        @field:SerializedName("categoryName")
        val categoryName: String = "",

        @field:SerializedName("itemLabel")
        val itemLabel: String = "",

        @field:SerializedName("itemType")
        val itemType: String = "",

        @field:SerializedName("operatorID")
        val operatorID: String = "",

        @field:SerializedName("productID")
        val productID: String = ""
)

data class UserTrackingData(
        @field:SerializedName("userType")
        val userType: String = ""
)

data class ProductInfo(
        @field:SerializedName("text")
        val text: String = "",

        @field:SerializedName("color")
        val color: String = ""
)