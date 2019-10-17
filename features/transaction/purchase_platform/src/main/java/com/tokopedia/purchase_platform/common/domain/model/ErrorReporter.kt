package com.tokopedia.purchase_platform.common.domain.model

import android.os.Parcel
import android.os.Parcelable

data class ErrorReporter (
        var eligible: Boolean = false,
        var texts: ErrorReporterText = ErrorReporterText()
): Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readByte() != 0.toByte(),
            parcel.readParcelable(ErrorReporterText::class.java.classLoader))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (eligible) 1 else 0)
        parcel.writeParcelable(texts, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ErrorReporter> {
        override fun createFromParcel(parcel: Parcel): ErrorReporter {
            return ErrorReporter(parcel)
        }

        override fun newArray(size: Int): Array<ErrorReporter?> {
            return arrayOfNulls(size)
        }
    }
}