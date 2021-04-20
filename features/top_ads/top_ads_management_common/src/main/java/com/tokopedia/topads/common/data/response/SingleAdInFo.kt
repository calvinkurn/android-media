package com.tokopedia.topads.common.data.response


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class SingleAdInFo(
        @SerializedName("topAdsGetPromo")
        val topAdsGetPromo: TopAdsGetPromo = TopAdsGetPromo()
)

data class TopAdsGetPromo(
        @SerializedName("data")
        val `data`: List<SingleAd> = listOf(),
        @SerializedName("errors")
        val errors: List<Error> = listOf()
)

@Parcelize
data class SingleAd(
        @SerializedName("adEndDate")
        var adEndDate: String = "",
        @SerializedName("adEndTime")
        var adEndTime: String = "",
        @SerializedName("adID")
        var adID: String = "",
        @SerializedName("adImage")
        var adImage: String = "",
        @SerializedName("adStartDate")
        var adStartDate: String = "",
        @SerializedName("adStartTime")
        var adStartTime: String = "",
        @SerializedName("adTitle")
        var adTitle: String = "",
        @SerializedName("adType")
        var adType: String = "",
        @SerializedName("cpmDetails")
        var cpmDetails: List<CpmDetail> = listOf(),
        @SerializedName("groupID")
        var groupID: String = "",
        @SerializedName("itemID")
        var itemID: String = "",
        @SerializedName("priceBid")
        var priceBid: Int = 0,
        @SerializedName("priceDaily")
        var priceDaily: Int = 0,
        @SerializedName("shopID")
        var shopID: String = "",
        @SerializedName("status")
        var status: String = ""
) : Parcelable

@Parcelize
data class CpmDetail(
        @SerializedName("description")
        var description: Description = Description(),
        @SerializedName("link")
        var link: String = "",
        @SerializedName("product")
        var product: List<TopAdsProductModel> = listOf(),
        @SerializedName("title")
        var title: String = ""
) : Parcelable

@Parcelize
data class Description(
        @SerializedName("slogan")
        var slogan: String = ""
) : Parcelable