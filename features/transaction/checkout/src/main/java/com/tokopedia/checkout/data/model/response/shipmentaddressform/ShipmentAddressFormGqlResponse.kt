package com.tokopedia.checkout.data.model.response.shipmentaddressform

import com.google.gson.annotations.SerializedName

data class ShipmentAddressFormGqlResponse(
        @SerializedName("shipment_address_form_v3")
        val shipmentAddressFormResponse: ShipmentAddressFormResponse = ShipmentAddressFormResponse()
)