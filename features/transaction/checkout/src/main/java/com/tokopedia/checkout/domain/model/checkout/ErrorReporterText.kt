package com.tokopedia.checkout.domain.model.checkout

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ErrorReporterText(
        var submitTitle: String = "",
        var submitDescription: String = "",
        var submitButton: String = "",
        var cancelButton: String = ""
): Parcelable