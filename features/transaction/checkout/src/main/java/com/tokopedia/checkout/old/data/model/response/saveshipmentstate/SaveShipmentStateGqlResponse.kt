package com.tokopedia.checkout.old.data.model.response.saveshipmentstate

import com.google.gson.annotations.SerializedName

data class SaveShipmentStateGqlResponse(
        @SerializedName("save_shipment")
        val saveShipmentStateResponse: SaveShipmentStateResponse
)