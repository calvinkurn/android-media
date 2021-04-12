package com.tokopedia.checkout.data.model.response.checkout

import com.google.gson.annotations.SerializedName

data class CheckoutResponse(
        @SerializedName("data")
        val data: CheckoutDataResponse = CheckoutDataResponse(),
        @SerializedName("error_reporter")
        val errorReporter: ErrorReporterResponse = ErrorReporterResponse()
)