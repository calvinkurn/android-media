package com.tokopedia.groupchat.chatroom.domain.pojo

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by StevenFredian on 03/05/18.
 */

data class ExitMessage(
        @SerializedName("title")
        @Expose
        var title: String = "",
        @SerializedName("body")
        @Expose
        var body: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "") {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(body)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ExitMessage> {
        override fun createFromParcel(parcel: Parcel): ExitMessage {
            return ExitMessage(parcel)
        }

        override fun newArray(size: Int): Array<ExitMessage?> {
            return arrayOfNulls(size)
        }
    }

}
