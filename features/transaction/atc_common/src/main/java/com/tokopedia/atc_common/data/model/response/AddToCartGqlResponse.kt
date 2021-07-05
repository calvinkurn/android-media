package com.tokopedia.atc_common.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 2019-07-12.
 */

data class AddToCartGqlResponse(
        @SerializedName("add_to_cart_v2")
        @Expose
        val addToCartResponse: AddToCartResponse = AddToCartResponse()
)
