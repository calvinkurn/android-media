package com.tokopedia.minicart.common.data.response.updatecart

import com.google.gson.annotations.SerializedName

data class UpdateCartGqlResponse(
        @SerializedName("update_cart_v2")
        val updateCartData: UpdateCartV2 = UpdateCartV2()
)