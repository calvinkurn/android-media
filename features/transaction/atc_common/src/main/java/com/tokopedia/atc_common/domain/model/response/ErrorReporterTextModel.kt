package com.tokopedia.atc_common.domain.model.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ErrorReporterTextModel(
        var submitTitle: String = "",
        var submitDescription: String = "",
        var submitButton: String = "",
        var cancelButton: String = ""
): Parcelable