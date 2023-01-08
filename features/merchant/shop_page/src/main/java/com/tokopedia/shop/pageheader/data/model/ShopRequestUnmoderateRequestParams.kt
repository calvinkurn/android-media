package com.tokopedia.shop.pageheader.data.model

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * author by Rafli Syam on 26/01/2021
 */
data class ShopRequestUnmoderateRequestParams(
    @SuppressLint("Invalid Data Type") // cannot use string or long since the shop ids is a list
    @SerializedName("shopIDs")
    @Expose
    var shopIds: MutableList<Double> = mutableListOf(),
    @SerializedName("status")
    @Expose
    var status: Int = 1,
    @SerializedName("responseDesc")
    @Expose
    var responseDescription: String = ""
)
