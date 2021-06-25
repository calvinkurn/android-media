package com.tokopedia.common.topupbills.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TopupBillsSeamlessFavNumberItem (
    @SerializedName("category_id")
    val categoryId: Int = -1,
    @SerializedName("client_number")
    val clientNumber: String = "",
    @SerializedName("label")
    val clientName: String = "",
    @SerializedName("operator_id")
    val operatorId: Int = -1,
    @SerializedName("product_id")
    val productId: Int = -1,
    @SerializedName("icon_url")
    val iconUrl: String = "",
    @SerializedName("favorite")
    val isFavorite: Boolean = true
): Parcelable