package com.tokopedia.oneclickcheckout.order.data.get

import com.google.gson.annotations.SerializedName

class GetOccCartGqlResponse(
        @SerializedName("get_occ_cart_page")
        val response: GetOccCartResponse = GetOccCartResponse()
)