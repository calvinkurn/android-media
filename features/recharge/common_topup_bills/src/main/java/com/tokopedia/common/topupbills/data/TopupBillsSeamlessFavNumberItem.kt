package com.tokopedia.common.topupbills.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TopupBillsSeamlessFavNumberItem (
        @SerializedName("category_id")
        val categoryId: String = "",
        @SerializedName("client_number")
        val clientNumber: String = "",
        @SerializedName("label")
        var clientName: String = "",
        @SerializedName("operator_id")
        val operatorId: String = "",
        @SerializedName("product_id")
        val productId: String = "",
        @SerializedName("icon_url")
        val iconUrl: String = ""
): Parcelable