package com.tokopedia.devicefingerprint.integrityapi.model

import com.google.gson.annotations.SerializedName

data class IntegrityApiResponse(
    @SerializedName("is_error")
    var isError: Boolean = false,
    @SerializedName("data")
    var integrityApiData: IntegrityApiData = IntegrityApiData(),
)

data class IntegrityApiData(
    @SerializedName("error_message")
    var errorMessage: String = ""
)