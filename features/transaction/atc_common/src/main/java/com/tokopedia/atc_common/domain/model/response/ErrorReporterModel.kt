package com.tokopedia.atc_common.domain.model.response

import android.os.Parcel
import android.os.Parcelable

data class ErrorReporterModel(
        var eligible: Boolean = false,
        var texts: ErrorReporterTextModel = ErrorReporterTextModel()
): Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readByte() != 0.toByte(),
            parcel.readParcelable(ErrorReporterTextModel::class.java.classLoader))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (eligible) 1 else 0)
        parcel.writeParcelable(texts, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ErrorReporterModel> {
        override fun createFromParcel(parcel: Parcel): ErrorReporterModel {
            return ErrorReporterModel(parcel)
        }

        override fun newArray(size: Int): Array<ErrorReporterModel?> {
            return arrayOfNulls(size)
        }
    }
}