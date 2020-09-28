package com.tokopedia.topads.dashboard.data.model.insightkey


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Button(
    @SerializedName("title")
    val title: String? = "",
    @SerializedName("url")
    val url: String? = ""
):Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Button> {
        override fun createFromParcel(parcel: Parcel): Button {
            return Button(parcel)
        }

        override fun newArray(size: Int): Array<Button?> {
            return arrayOfNulls(size)
        }
    }
}