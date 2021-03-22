package com.tokopedia.topads.common.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TopAdsProductModel(
        @SerializedName("productID")
        val productID: String = "0",
        @SerializedName("productName")
        val productName: String = "",
        @SerializedName("productPrice")
        val productPrice: String = "",
        @SerializedName("productPriceNum")
        val productPriceNum: Int = 0,
        @SerializedName("productImage")
        val productImage: String = "",
        @SerializedName("productIsPromoted")
        val productIsPromoted: Boolean = false,
        @SerializedName("productURI", alternate = ["productURL"])
        val productUri: String = "",
        @SerializedName("adID")
        val adID: String = "0",
        @SerializedName("adStatus")
        val adStatus: Int = 0,
        @SerializedName("groupName")
        val groupName: String = "",
        @SerializedName("groupID")
        val groupID: Int = 0,
        @SerializedName("departmentName")
        val departmentName: String = "",
        @SerializedName("departmentID")
        val departmentID: Int = 0,
        @SerializedName("productRating")
        val productRating: Int = 0,
        @SerializedName("productReviewCount")
        val productReviewCount: Int = 0,
        @SerializedName("productActive")
        var productActive: Int = 0,
        var isRecommended: Boolean = false,
        var isSingleSelect: Boolean = false,
        var positionInRv: Int = -1) : Parcelable {
    override fun equals(other: Any?): Boolean {
        return other is TopAdsProductModel && other.productID == this.productID && other.productName == this.productName
    }

    override fun hashCode(): Int {
        return this.productID.hashCode()
    }
}
