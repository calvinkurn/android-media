package com.tokopedia.checkout.old.data.model.response.changeshippingaddress

import com.google.gson.annotations.SerializedName

data class ChangeShippingAddressResponse(
        @SerializedName("data")
        val dataResponse: ChangeShippingAddressDataResponse
)