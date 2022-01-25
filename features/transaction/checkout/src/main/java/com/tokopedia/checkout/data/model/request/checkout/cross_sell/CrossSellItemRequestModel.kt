package com.tokopedia.checkout.data.model.request.checkout.cross_sell

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CrossSellItemRequestModel(
        @SerializedName("id")
        var id: Int = 0,

        @SerializedName("price")
        var price: Int = -1,

        @SerializedName("transaction_type")
        var transactionType: String = "",

        @SerializedName("additional_vertical_id")
        var additionalVerticalId: Int = -1
) : Parcelable
