package com.tokopedia.one.click.checkout.order.data

import com.google.gson.annotations.SerializedName

data class GetOccCartGqlResponse(
        @SerializedName("get_occ_cart_page")
        val response: GetOccCartResponse = GetOccCartResponse()
)