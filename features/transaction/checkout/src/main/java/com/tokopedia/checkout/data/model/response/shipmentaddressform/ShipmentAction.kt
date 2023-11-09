package com.tokopedia.checkout.data.model.response.shipmentaddressform

import com.google.gson.annotations.SerializedName

data class ShipmentAction(
    @SerializedName("sp_id")
    val spId: Long = 0,
    @SerializedName("action")
    val action: String = "",
    @SerializedName("popup")
    val popup: ShipmentActionPopup = ShipmentActionPopup()
) {

    data class ShipmentActionPopup(
        @SerializedName("title")
        val title: String = "",
        @SerializedName("body")
        val body: String = "",
        @SerializedName("button_ok")
        val buttonOk: String = "",
        @SerializedName("button_cancel")
        val buttonCancel: String = ""
    )
}
