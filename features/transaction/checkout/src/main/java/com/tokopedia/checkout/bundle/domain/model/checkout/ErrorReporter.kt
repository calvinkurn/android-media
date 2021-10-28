package com.tokopedia.checkout.bundle.domain.model.checkout

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ErrorReporter (
        var eligible: Boolean = false,
        var texts: ErrorReporterText = ErrorReporterText()
): Parcelable