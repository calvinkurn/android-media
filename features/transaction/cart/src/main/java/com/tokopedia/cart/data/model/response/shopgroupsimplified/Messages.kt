package com.tokopedia.cart.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

data class Messages(
        @SerializedName("ErrorCheckoutPriceLimit")
        val errorCheckoutPriceLimit: String = "",
        @SerializedName("ErrorFieldBetween")
        val errorFieldBetween: String = "",
        @SerializedName("ErrorFieldMaxChar")
        val errorFieldMaxChar: String = "",
        @SerializedName("ErrorFieldRequired")
        val errorFieldRequired: String = "",
        @SerializedName("ErrorProductAvailableStock")
        val errorProductAvailableStock: String = "",
        @SerializedName("ErrorProductAvailableStockDetail")
        val errorProductAvailableStockDetail: String = "",
        @SerializedName("ErrorProductMaxQuantity")
        val errorProductMaxQuantity: String = "",
        @SerializedName("ErrorProductMinQuantity")
        val errorProductMinQuantity: String = ""
)
