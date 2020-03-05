package com.tokopedia.atc_common.data.model.response

import com.google.gson.annotations.SerializedName

data class AddToCartOccGqlResponse(
        @SerializedName("add_to_cart_occ")
        val addToCartOccResponse: AddToCartOccResponse = AddToCartOccResponse()
)