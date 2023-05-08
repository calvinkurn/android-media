package com.tokopedia.checkout.data.model.response.shipmentaddressform

import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 26/10/21.
 */
data class CrossSellBottomSheet(
    @SerializedName("title")
    val title: String = "",

    @SerializedName("sub_title")
    val subtitle: String = ""
)
