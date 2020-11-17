package com.tokopedia.buyerorder.detail.data

import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 12/11/20.
 */
data class Discount (
        @SerializedName("label")
        val label: String = "",

        @SerializedName("value")
        val value: String = "",

        @SerializedName("textColor")
        val textColor: String = "",

        @SerializedName("backgroundColor")
        val bgColor: String = "",

        @SerializedName("imageUrl")
        val imgUrl: String = "")