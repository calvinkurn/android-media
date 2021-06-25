package com.tokopedia.common.topupbills.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TopupBillsSeamlessFavNumber(
        @SerializedName("category_id")
        val categoryId: Int = -1,
        @SerializedName("client_number")
        val clientNumber: String = "",
        @SerializedName("list")
        val favoriteNumbers: List<TopupBillsSeamlessFavNumberItem> = listOf(),
        @SerializedName("operator_id")
        val operatorId: Int = -1,
        @SerializedName("product_id")
        val productId: Int = -1
): Parcelable