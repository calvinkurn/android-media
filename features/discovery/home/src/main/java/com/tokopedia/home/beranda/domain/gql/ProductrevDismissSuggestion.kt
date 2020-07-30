package com.tokopedia.home.beranda.domain.gql

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevDismissSuggestion(
        @SerializedName("productrevDismissSuggestion")
        @Expose
        val statusDismiss: String = ""
)