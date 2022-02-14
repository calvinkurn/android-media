package com.tokopedia.common.topupbills.favorite.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TopupBillsPersoFavNumberTrackingData(
    @SerializedName("clientNumber")
    val clientNumber: String = "",
    @SerializedName("lastOrderDate")
    val lastOrderDate: String = "",
    @SerializedName("totalTransaction")
    val totalTransaction: String = "",
    @SerializedName("categoryID")
    val categoryId: String = "",
    @SerializedName("operatorID")
    val operatorId: String = "",
    @SerializedName("productID")
    val productId: String = ""
): Parcelable