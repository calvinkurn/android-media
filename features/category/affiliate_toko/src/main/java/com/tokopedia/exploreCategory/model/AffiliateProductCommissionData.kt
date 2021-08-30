package com.tokopedia.exploreCategory.model

import android.annotation.SuppressLint
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class AffiliateProductCommissionData(
        @SerializedName("status") val status : Boolean?,
        @SerializedName("commission") val commission : List<Commission>?,
        @SerializedName("error") val error : Error?
) {
    data class Commission (
            @SerializedName("productID") val productID : Int?,
            @SerializedName("shopID") val shopID : Int?,
            @SerializedName("categoryID") val categoryID : Int?,
            @SerializedName("priceFormatted") val priceFormatted : String?,
            @SuppressLint("Invalid Data Type") @SerializedName("price") val price : Double?,
            @SerializedName("amountFormatted") val amountFormatted : String?,
            @SerializedName("amount") val amount : Int?,
            @SerializedName("percentageFormatted") val percentageFormatted : String?,
            @SerializedName("percentage") val percentage : Int?
    )

    data class Error(val error : String){

    }
}