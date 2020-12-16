package com.tokopedia.topads.common.data.response


import com.google.gson.annotations.SerializedName

data class SingleAdInFo(
        @SerializedName("topAdsGetPromo")
        val topAdsGetPromo: TopAdsGetPromo = TopAdsGetPromo()
)

data class TopAdsGetPromo(
        @SerializedName("data")
        val `data`: List<SingleAd> = listOf(),
        @SerializedName("errors")
        val errors: List<Any> = listOf()
)

data class SingleAd(
        @SerializedName("adEndDate")
        var adEndDate: String,
        @SerializedName("adEndTime")
        var adEndTime: String,
        @SerializedName("adID")
        var adID: String,
        @SerializedName("adImage")
        var adImage: String,
        @SerializedName("adStartDate")
        var adStartDate: String,
        @SerializedName("adStartTime")
        var adStartTime: String,
        @SerializedName("adTitle")
        var adTitle: String,
        @SerializedName("adType")
        var adType: String,
        @SerializedName("cpmDetails")
        var cpmDetails: List<CpmDetail>,
        @SerializedName("groupID")
        var groupID: String,
        @SerializedName("itemID")
        var itemID: String,
        @SerializedName("priceBid")
        var priceBid: Int,
        @SerializedName("priceDaily")
        var priceDaily: Int,
        @SerializedName("shopID")
        var shopID: String,
        @SerializedName("status")
        var status: String
)

data class CpmDetail(
        @SerializedName("description")
        var description: Description,
        @SerializedName("link")
        var link: String,
        @SerializedName("product")
        var product: List<Product>,
        @SerializedName("title")
        var title: String
)

data class Description(
        @SerializedName("slogan")
        var slogan: String
)

data class Product(
        @SerializedName("departmentID")
        var departmentID: Int,
        @SerializedName("departmentName")
        var departmentName: String,
        @SerializedName("productActive")
        var productActive: Int,
        @SerializedName("productID")
        var productID: String,
        @SerializedName("productImage")
        var productImage: String,
        @SerializedName("productName")
        var productName: String,
        @SerializedName("productPrice")
        var productPrice: String,
        @SerializedName("productRating")
        var productRating: Int,
        @SerializedName("productReviewCount")
        var productReviewCount: Int,
        @SerializedName("productURL")
        var productURL: String
)