package com.tokopedia.chatbot.chatbot2.data.csatRating.websocketCsatRatingResponse

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class FallbackAttachment(

    @SerializedName("html")
    val html: String? = null,

    @SerializedName("message")
    val message: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(html)
        parcel.writeString(message)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FallbackAttachment> {
        override fun createFromParcel(parcel: Parcel): FallbackAttachment {
            return FallbackAttachment(parcel)
        }

        override fun newArray(size: Int): Array<FallbackAttachment?> {
            return arrayOfNulls(size)
        }
    }
}
