package com.tokopedia.atc_common.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 2019-07-12.
 */

data class AddToCartOcsGqlResponse(
        @SerializedName("atcOCS")
        @Expose
        val addToCartResponse: AddToCartResponse = AddToCartResponse()
)
