package com.tokopedia.officialstore.official.data.model.dynamic_channel

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Created by Lukas on 3/23/21.
 */
data class LabelGroup(
        @SerializedName("position")
        val position: String = "",
        @SerializedName("title")
        val title: String = "",
        @SerializedName("type")
        val type: String = "",
        @SerializedName("url")
        val imageUrl: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "") {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(position)
        parcel.writeString(title)
        parcel.writeString(type)
        parcel.writeString(imageUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LabelGroup> {
        override fun createFromParcel(parcel: Parcel): LabelGroup {
            return LabelGroup(parcel)
        }

        override fun newArray(size: Int): Array<LabelGroup?> {
            return arrayOfNulls(size)
        }
    }
}