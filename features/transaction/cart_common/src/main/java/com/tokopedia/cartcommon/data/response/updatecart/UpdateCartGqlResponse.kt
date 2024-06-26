package com.tokopedia.cartcommon.data.response.updatecart

import com.google.gson.annotations.SerializedName

data class UpdateCartGqlResponse(
        @SerializedName("update_cart_v2")
        val updateCartData: UpdateCartV2Data = UpdateCartV2Data()
)