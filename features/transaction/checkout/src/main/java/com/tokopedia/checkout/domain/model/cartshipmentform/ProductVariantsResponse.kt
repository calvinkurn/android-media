package com.tokopedia.checkout.domain.model.cartshipmentform

import com.google.gson.annotations.SerializedName

data class ProductVariantsResponse(
        @SerializedName("parent_id")
        var parentId: String = ""
)
