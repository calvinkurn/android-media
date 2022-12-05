package com.tokopedia.checkout.data.model.request.checkout.cross_sell

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CrossSellItemRequestModel(
        @SuppressLint("Invalid Data Type")
        @SerializedName("id")
        var id: Int = 0,

        @SuppressLint("Invalid Data Type")
        @SerializedName("price")
        var price: Int = -1,

        @SerializedName("transaction_type")
        var transactionType: String = "",

        @SuppressLint("Invalid Data Type")
        @SerializedName("additional_vertical_id")
        var additionalVerticalId: Int = -1
) : Parcelable
