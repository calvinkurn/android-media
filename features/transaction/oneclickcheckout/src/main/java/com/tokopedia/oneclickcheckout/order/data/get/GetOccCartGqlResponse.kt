package com.tokopedia.oneclickcheckout.order.data.get

import com.google.gson.annotations.SerializedName

class GetOccCartGqlResponse(
        @SerializedName("get_occ_multi")
        val response: GetOccCartResponse = GetOccCartResponse()
)