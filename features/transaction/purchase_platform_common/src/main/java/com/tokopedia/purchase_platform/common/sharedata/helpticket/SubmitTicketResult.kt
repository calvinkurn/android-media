package com.tokopedia.purchase_platform.common.sharedata.helpticket

import android.os.Parcel
import android.os.Parcelable

data class SubmitTicketResult(
        var status: Boolean = true,
        var message: String = "",
        var texts: SubmitTicketText = SubmitTicketText()
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readByte() != 0.toByte(),
            parcel.readString() ?: "",
            parcel.readParcelable(SubmitTicketText::class.java.classLoader) ?: SubmitTicketText())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (status) 1 else 0)
        parcel.writeString(message)
        parcel.writeParcelable(texts, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SubmitTicketResult> {
        override fun createFromParcel(parcel: Parcel): SubmitTicketResult {
            return SubmitTicketResult(parcel)
        }

        override fun newArray(size: Int): Array<SubmitTicketResult?> {
            return arrayOfNulls(size)
        }
    }
}