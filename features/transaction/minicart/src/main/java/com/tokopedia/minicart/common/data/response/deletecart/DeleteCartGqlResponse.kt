package com.tokopedia.minicart.common.data.response.deletecart

import com.google.gson.annotations.SerializedName

data class DeleteCartGqlResponse(
        @SerializedName("remove_from_cart")
        val removeFromCart: RemoveFromCartData = RemoveFromCartData()
)