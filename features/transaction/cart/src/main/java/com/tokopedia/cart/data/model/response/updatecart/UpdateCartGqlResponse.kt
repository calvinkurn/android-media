package com.tokopedia.cart.data.model.response.updatecart

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 2019-12-26.
 */

data class UpdateCartGqlResponse(
        @SerializedName("update_cart_v2")
        val updateCartDataResponse: UpdateCartDataResponse = UpdateCartDataResponse()
)