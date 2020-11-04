package com.tokopedia.seller.search.common.domain.model

import com.google.gson.annotations.SerializedName

data class SellerSearchPlaceholderResponse(
    @SerializedName("placeholder")
    val response: SellerSearchPlaceholder
) {

    data class SellerSearchPlaceholder(
        @SerializedName("sentence")
        val sentence: String
    )
}