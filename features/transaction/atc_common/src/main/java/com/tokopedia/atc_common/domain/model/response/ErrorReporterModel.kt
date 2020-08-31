package com.tokopedia.atc_common.domain.model.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ErrorReporterModel(
        var eligible: Boolean = false,
        var texts: ErrorReporterTextModel = ErrorReporterTextModel()
): Parcelable