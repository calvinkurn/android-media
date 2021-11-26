package com.tokopedia.checkout.old.domain.model.checkout

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ErrorReporter (
        var eligible: Boolean = false,
        var texts: ErrorReporterText = ErrorReporterText()
): Parcelable