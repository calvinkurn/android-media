package com.tokopedia.one.click.checkout.order.data

import com.google.gson.annotations.SerializedName

data class UpdateCartOccGqlResponse(
        @SerializedName("update_cart_occ")
        var response: UpdateCartOccResponse
)