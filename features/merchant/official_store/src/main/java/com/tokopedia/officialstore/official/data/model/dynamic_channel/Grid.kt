package com.tokopedia.officialstore.official.data.model.dynamic_channel

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Grid(
        @Expose @SerializedName("freeOngkir") val freeOngkir: FreeOngkir?,
        @Expose @SerializedName("id") val id: Long,
        @Expose @SerializedName("name") val name: String,
        @Expose @SerializedName("applink") val applink: String,
        @Expose @SerializedName("price") val price: String,
        @Expose @SerializedName("slashedPrice") val slashedPrice: String,
        @Expose @SerializedName("discount") val discount: String,
        @Expose @SerializedName("imageUrl") val imageUrl: String,
        @Expose @SerializedName("productImageUrl") val productImageUrl: String,
        @Expose @SerializedName("back_color") val backColor: String,
        @Expose @SerializedName("rating") val rating: Int,
        @Expose @SerializedName("ratingAverage") val ratingAverage: String? = "",
        @Expose @SerializedName("count_review") val countReview: Int,
        @Expose @SerializedName("warehouseID") val warehouseID: String,
        @Expose @SerializedName("label") val label: String,
        @Expose @SerializedName("soldPercentage") val soldPercentage: Long,
        @Expose @SerializedName("attribution") val attribution: String,
        @Expose @SerializedName("productClickUrl") val productClickUrl: String,
        @Expose @SerializedName("impression") val impression: String,
        @Expose @SerializedName("cashback") val cashback: String,
        @Expose @SerializedName("discountPercentage") val discountPercentage: String,
        @Expose @SerializedName("labelGroup") val labelGroup: List<LabelGroup> = arrayListOf(),
        // Impression purposed
        var isImpressed: Boolean = false
) : Parcelable