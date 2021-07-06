package com.tokopedia.common.topupbills.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class TopupBillsSeamlessFavNumberModData(
    @SerializedName("updateFavoriteDetail")
    val updateFavoriteDetail: UpdateFavoriteDetail
)

@Parcelize
data class UpdateFavoriteDetail(
    @SerializedName("categoryID")
    val categoryID: Int,
    @SerializedName("clientNumber")
    val clientNumber: String,
    @SerializedName("label")
    val label: String,
    @SerializedName("lastOrderDate")
    val lastOrderDate: String,
    @SerializedName("lastUpdated")
    val lastUpdated: String,
    @SerializedName("operatorID")
    val operatorID: Int,
    @SerializedName("productID")
    val productID: Int,
    @SerializedName("subscribed")
    val subscribed: Boolean,
    @SerializedName("totalTransaction")
    val totalTransaction: Int,
    @SerializedName("wishlist")
    val wishlist: Boolean
): Parcelable