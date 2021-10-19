package com.tokopedia.checkout.bundle.data.model.response.changeshippingaddress

import com.google.gson.annotations.SerializedName

data class ChangeShippingAddressGqlResponse(
        @SerializedName("change_address_cart")
        val changeShippingAddressResponse: ChangeShippingAddressResponse
)