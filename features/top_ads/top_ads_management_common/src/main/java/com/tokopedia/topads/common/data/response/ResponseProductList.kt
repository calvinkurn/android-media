package com.tokopedia.topads.common.data.response


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class ResponseProductList(
        @SerializedName("data")
        val result: Result = Result()
) {
    data class Result(
            @SerializedName("topadsGetListProduct")
            val topadsGetListProduct: TopadsGetListProduct = TopadsGetListProduct()
    ) {
        data class TopadsGetListProduct(
                @SerializedName("data")
                val `data`: List<Data> = listOf(),
                @SerializedName("eof")
                val eof: Boolean = false,
                @SerializedName("errors")
                val errors: List<Error> = listOf()
        ) {
            @Parcelize
            data class Data(
                    @SerializedName("productID")
                    val productID: Int = 0,
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
                    @SerializedName("productURI")
                    val productUri: String = "",
                    @SerializedName("adID")
                    val adID: Int = 0,
                    @SerializedName("adStatus")
                    val adStatus: Int = 0,
                    @SerializedName("groupName")
                    val groupName: String = "",
                    @SerializedName("groupID")
                    val groupID: Int = 0,
                    @SerializedName("departmentName")
                    val departmentName: String = "",
                    @SerializedName("departmentID")
                    val departmentId: Int = 0,
                    @SerializedName("suggestedBid")
                    val suggestedBid: Int = 0,
                    @SerializedName("priceBid")
                    val priceBid: Int = 0,
                    @SerializedName("productRating")
                    val productRating: Int = 0,
                    @SerializedName("productReviewCount")
                    val productReviewCount: Int = 0,
                    var isRecommended: Boolean = false,
                    var isSingleSelect: Boolean = false,
                    var positionInRv: Int = -1
            ) : Parcelable {
                override fun equals(other: Any?): Boolean {
                    return other is Data && other.productID == this.productID && other.productName == this.productName
                }

                override fun hashCode(): Int {
                    return this.productID.hashCode()
                }
            }
        }
    }
}