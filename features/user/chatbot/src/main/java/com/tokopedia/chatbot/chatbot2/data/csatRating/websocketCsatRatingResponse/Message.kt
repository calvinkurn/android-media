package com.tokopedia.chatbot.chatbot2.data.csatRating.websocketCsatRatingResponse

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Message(

    @SerializedName("censored_reply")
    val censoredReply: String? = null,

    @SerializedName("timestamp_unix")
    val timestampUnix: Long? = null,

    @SerializedName("timestamp_fmt")
    val timestampFmt: String? = null,

    @SerializedName("original_reply")
    val originalReply: String? = null,

    @SerializedName("timestamp_unix_nano")
    val timestampUnixNano: Long? = null,

    @SerializedName("timestamp")
    val timestamp: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(censoredReply)
        parcel.writeValue(timestampUnix)
        parcel.writeString(timestampFmt)
        parcel.writeString(originalReply)
        parcel.writeValue(timestampUnixNano)
        parcel.writeString(timestamp)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Message> {
        override fun createFromParcel(parcel: Parcel): Message {
            return Message(parcel)
        }

        override fun newArray(size: Int): Array<Message?> {
            return arrayOfNulls(size)
        }
    }
}
