package com.tokopedia.privacycenter.dsar.model

import com.google.gson.annotations.SerializedName

data class CreateRequestBody (
    @SerializedName("email")
    val email: String = ""
)
