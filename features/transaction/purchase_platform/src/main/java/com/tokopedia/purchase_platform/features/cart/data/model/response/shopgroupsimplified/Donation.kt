package com.tokopedia.purchase_platform.features.cart.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author Irfan Khoirul on 16/08/18.
 */

data class Donation(
    @SerializedName("Title")
    @Expose
    val title: String = "",
    @SerializedName("Nominal")
    @Expose
    val nominal: Int = 0,
    @SerializedName("Description")
    @Expose
    val description: String = ""
)
