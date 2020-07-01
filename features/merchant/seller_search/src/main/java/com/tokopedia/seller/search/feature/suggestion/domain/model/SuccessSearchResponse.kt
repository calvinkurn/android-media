package com.tokopedia.seller.search.feature.suggestion.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SuccessSearchResponse(
        @Expose
        @SerializedName("successSearch")
        val successSearch: SuccessSearch = SuccessSearch()
) {
    data class SuccessSearch(
            @Expose
            @SerializedName("message")
            val message: String? = "",
            @Expose
            @SerializedName("status")
            val status: String? = ""
    )
}