package com.tokopedia.privacycenter.dsar.model

import com.google.gson.annotations.SerializedName

data class SearchRequestBody(
    @SerializedName("term")
    var email: String = "",
)
