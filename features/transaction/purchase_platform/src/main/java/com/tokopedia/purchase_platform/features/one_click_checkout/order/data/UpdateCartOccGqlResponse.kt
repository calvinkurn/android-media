package com.tokopedia.purchase_platform.features.one_click_checkout.order.data

import com.google.gson.annotations.SerializedName

data class UpdateCartOccGqlResponse(
        @SerializedName("update_cart_occ")
        var response: UpdateCartOccResponse
)