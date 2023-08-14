package com.tokopedia.atc_common.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ErrorReporterTextResponse(
    @SerializedName("submit_title")
    @Expose
    val submitTitle: String = "",

    @SerializedName("submit_description")
    @Expose
    val submitDescription: String = "",

    @SerializedName("submit_button")
    @Expose
    val submitButton: String = "",

    @SerializedName("cancel_button")
    @Expose
    val cancelButton: String = ""
)
