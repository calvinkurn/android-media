package com.tokopedia.cart.data.model.response.shopgroupsimplified

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class AddOnWidget(
    @SerializedName("wording")
    val wording: String = "",
    @SerializedName("left_icon_url")
    val leftIconUrl: String = "",
    @SerializedName("right_icon_url")
    val rightIconUrl: String = "",
    @SerializedName("title")
    val title: String = "",
    @SuppressLint("Invalid Data Type")
    @SerializedName("price")
    val price: Long = 0
)
