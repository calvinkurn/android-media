package com.tokopedia.seller.search.feature.suggestion.data

import com.google.gson.annotations.SerializedName

data class SuccessSearchResponse(
        @SerializedName("successSearch")
        val successSearch: SuccessSearch = SuccessSearch()
) {
    data class SuccessSearch(
            @SerializedName("message")
            val message: String? = "",
            @SerializedName("status")
            val status: String? = ""
    )
}