package com.tokopedia.atc_common.data.model.response

import com.google.gson.annotations.SerializedName

data class AddToCartOccExternalGqlResponse(
        @SerializedName("add_to_cart_occ_external")
        val addToCartOccResponse: AddToCartOccResponse = AddToCartOccResponse()
)