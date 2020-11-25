package com.tokopedia.cart.data.model.response.shopgroupsimplified

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author anggaprasetiyo on 31/01/18.
 */

data class Messages(
    @SerializedName("ErrorCheckoutPriceLimit")
    @Expose
    val errorCheckoutPriceLimit: String = "",
    @SerializedName("ErrorFieldBetween")
    @Expose
    val errorFieldBetween: String = "",
    @SerializedName("ErrorFieldMaxChar")
    @Expose
    val errorFieldMaxChar: String = "",
    @SerializedName("ErrorFieldRequired")
    @Expose
    val errorFieldRequired: String = "",
    @SerializedName("ErrorProductAvailableStock")
    @Expose
    val errorProductAvailableStock: String = "",
    @SerializedName("ErrorProductAvailableStockDetail")
    @Expose
    val errorProductAvailableStockDetail: String = "",
    @SerializedName("ErrorProductMaxQuantity")
    @Expose
    val errorProductMaxQuantity: String = "",
    @SerializedName("ErrorProductMinQuantity")
    @Expose
    val errorProductMinQuantity: String = ""
)
