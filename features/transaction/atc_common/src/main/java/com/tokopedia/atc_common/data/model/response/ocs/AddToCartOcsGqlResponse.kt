package com.tokopedia.atc_common.data.model.response.ocs

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AddToCartOcsGqlResponse(
    @SerializedName("atcOCS")
    @Expose
    val addToCartResponse: AddToCartOcsResponse = AddToCartOcsResponse()
)
