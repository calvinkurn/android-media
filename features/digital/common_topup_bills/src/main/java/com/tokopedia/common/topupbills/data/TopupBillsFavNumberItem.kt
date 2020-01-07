package com.tokopedia.common.topupbills.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class TopupBillsFavNumberItem(
        @SerializedName("client_number")
        @Expose
        var clientNumber: String = "",
        @SerializedName("label")
        @Expose
        var label: String= "",
        @SerializedName("product_id")
        @Expose
        var productId: String = "",
        @SerializedName("operator_id")
        @Expose
        var operatorId: String = "",
        @SerializedName("category_id")
        @Expose
        var categoryId: String = "",
        @SerializedName("favorite")
        @Expose
        var isFavorite: Boolean = true) : Parcelable