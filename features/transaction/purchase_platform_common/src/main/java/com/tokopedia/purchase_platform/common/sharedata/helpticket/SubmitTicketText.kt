package com.tokopedia.purchase_platform.common.sharedata.helpticket

import android.os.Parcel
import android.os.Parcelable

data class SubmitTicketText(
        var submitTitle: String = "",
        var submitDescription: String = "",
        var successButton: String = ""
): Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(submitTitle)
        parcel.writeString(submitDescription)
        parcel.writeString(successButton)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SubmitTicketText> {
        override fun createFromParcel(parcel: Parcel): SubmitTicketText {
            return SubmitTicketText(parcel)
        }

        override fun newArray(size: Int): Array<SubmitTicketText?> {
            return arrayOfNulls(size)
        }
    }
}