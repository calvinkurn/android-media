package com.tokopedia.autocompletecomponent.unify.domain.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class SuggestionUnifyProduct(
    @SerializedName("discount")
    @Expose
    val discount: String = "",

    @SerializedName("original_price")
    @Expose
    val originalPrice: String = ""
)
