package com.tokopedia.checkout.old.domain.model.checkout

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ErrorReporterText(
        var submitTitle: String = "",
        var submitDescription: String = "",
        var submitButton: String = "",
        var cancelButton: String = ""
): Parcelable