package com.tokopedia.seller.search.common.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SellerSearchPlaceholderResponse(
    @Expose
    @SerializedName("placeholder")
    val response: SellerSearchPlaceholder
) {

    data class SellerSearchPlaceholder(
        @Expose
        @SerializedName("sentence")
        val sentence: String
    )
}