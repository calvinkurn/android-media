package com.tokopedia.csat_rating.data

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.SerializedName

class BadCsatReasonListItem() : Parcelable {
    @SerializedName("messageEn")
    var messageEn: String? = null
    @SerializedName("id")
    var id: Long = 0
    @SerializedName("message")
    var message: String? = null

    fun getMessageCsatReason() = message.orEmpty()

    constructor(parcel: Parcel) : this() {
        messageEn = parcel.readString()
        id = parcel.readLong()
        message = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(messageEn)
        parcel.writeLong(id)
        parcel.writeString(message)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BadCsatReasonListItem> {
        override fun createFromParcel(parcel: Parcel): BadCsatReasonListItem {
            return BadCsatReasonListItem(parcel)
        }

        override fun newArray(size: Int): Array<BadCsatReasonListItem?> {
            return arrayOfNulls(size)
        }
    }
}
