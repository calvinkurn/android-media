package com.tokopedia.atc_common.domain.model.response

import android.os.Parcel
import android.os.Parcelable

data class ErrorReporterTextModel(
        var submitTitle: String = "",
        var submitDescription: String = "",
        var submitButton: String = "",
        var cancelButton: String = ""
): Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(submitTitle)
        parcel.writeString(submitDescription)
        parcel.writeString(submitButton)
        parcel.writeString(cancelButton)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ErrorReporterTextModel> {
        override fun createFromParcel(parcel: Parcel): ErrorReporterTextModel {
            return ErrorReporterTextModel(parcel)
        }

        override fun newArray(size: Int): Array<ErrorReporterTextModel?> {
            return arrayOfNulls(size)
        }
    }
}