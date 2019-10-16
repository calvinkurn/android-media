package com.tokopedia.purchase_platform.features.checkout.data.model.response.checkout

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CheckoutResponse(
        @SerializedName("data")
        @Expose
        val data: CheckoutDataResponse = CheckoutDataResponse(),

        @SerializedName("error_reporter")
        @Expose
        val errorReporter: ErrorReporterResponse = ErrorReporterResponse()
)