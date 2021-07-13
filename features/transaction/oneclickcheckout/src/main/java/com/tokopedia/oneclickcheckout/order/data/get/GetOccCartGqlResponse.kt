package com.tokopedia.oneclickcheckout.order.data.get

import com.google.gson.annotations.SerializedName

class GetOccCartGqlResponse(
        @SerializedName("get_occ_multi_page")
        val response: GetOccCartResponse = GetOccCartResponse()
)