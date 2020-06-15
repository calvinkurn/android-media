package com.tokopedia.oneclickcheckout.order.data

import com.google.gson.annotations.SerializedName

data class UpdateCartOccGqlResponse(
        @SerializedName("update_cart_occ")
        var response: UpdateCartOccResponse
)