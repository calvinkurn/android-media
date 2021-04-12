package com.tokopedia.checkout.domain.model.checkout

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CheckoutData(
        var isError: Boolean = false,
        var errorMessage: String = "",
        var paymentId: String = "",
        var queryString: String = "",
        var redirectUrl: String = "",
        var callbackSuccessUrl: String = "",
        var callbackFailedUrl: String = "",
        var transactionId: String = "",
        var errorReporter: ErrorReporter = ErrorReporter(),
        var jsonResponse: String = "",
        var priceValidationData: PriceValidationData = PriceValidationData()
) : Parcelable