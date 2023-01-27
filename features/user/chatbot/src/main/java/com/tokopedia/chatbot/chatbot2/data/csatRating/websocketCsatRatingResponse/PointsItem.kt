package com.tokopedia.chatbot.chatbot2.data.csatRating.websocketCsatRatingResponse

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class PointsItem(

    @SerializedName("score")
    val score: Long? = null,

    @SerializedName("caption")
    val caption: String? = null,

    @SerializedName("description")
    val description: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(score)
        parcel.writeString(caption)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PointsItem> {
        override fun createFromParcel(parcel: Parcel): PointsItem {
            return PointsItem(parcel)
        }

        override fun newArray(size: Int): Array<PointsItem?> {
            return arrayOfNulls(size)
        }
    }
}
