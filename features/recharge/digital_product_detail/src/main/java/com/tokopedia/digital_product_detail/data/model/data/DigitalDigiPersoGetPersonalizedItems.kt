package com.tokopedia.digital_product_detail.data.model.data

import com.google.gson.annotations.SerializedName

data class DigitalDigiPersoGetPersonalizedItem(
    @SerializedName("digiPersoGetPersonalizedItems")
    val recommendationData: PersoRecommendationData
)

data class PersoRecommendationData(
    @SerializedName("title")
    val title: String,
    @SerializedName("mediaURL")
    val mediaUrl: String,
    @SerializedName("items")
    val items: List<PersoRecommendationItem>,
    @SerializedName("textLink")
    val textLink: String,
    @SerializedName("webLink")
    val webLink: String,
    @SerializedName("appLink")
    val appLink: String
)

data class PersoRecommendationItem(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("mediaURL")
    val mediaURL: String,
    @SerializedName("mediaUrlType")
    val mediaUrlType: String,
    @SerializedName("label1")
    val label1: String,
    @SerializedName("label2")
    val label2: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("discount")
    val discount: String,
    @SerializedName("slashPrice")
    val slashPrice: String,
    @SerializedName("appLink")
    val appLink: String,
    @SerializedName("webLink")
    val webLink: String,
    @SerializedName("backgroundColor")
    val backgroundColor: String,
    @SerializedName("trackingData")
    val trackingData: TrackingData
)

data class TrackingData(
    @SerializedName("businessUnit")
    val businessUnit: String,
    @SerializedName("categoryID")
    val categoryId: String,
    @SerializedName("categoryName")
    val categoryName: String,
    @SerializedName("clientNumber")
    val clientNumber: String,
    @SerializedName("itemLabel")
    val itemLabel: String,
    @SerializedName("itemType")
    val itemType: String,
    @SerializedName("operatorID")
    val operatorId: String,
    @SerializedName("productID")
    val productId: String
)