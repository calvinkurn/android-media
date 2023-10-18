package com.tokopedia.checkout.data.model.response.shipmentaddressform

import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 26/10/21.
 */
data class CrossSellInfoData(
    @SerializedName("title")
    val title: String = "",

    @SerializedName("sub_title")
    val subtitle: String = "",

    @SerializedName("tooltip_text")
    val tooltipText: String = "",

    @SerializedName("icon_url")
    val iconUrl: String = ""
)
