package com.tokopedia.purchase_platform.features.checkout.data.model.response.checkout

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.purchase_platform.features.checkout.data.model.response.checkout.ErrorReporterTextResponse

data class ErrorReporterResponse(

        @SerializedName("eligible")
        @Expose
        var eligible: Boolean = false,

        @SerializedName("texts")
        @Expose
        var texts: ErrorReporterTextResponse = ErrorReporterTextResponse()
)