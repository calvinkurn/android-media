package com.tokopedia.topads.dashboard.data.model.insightkey


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Box(
    @SerializedName("button")
    val button: Button? = Button(),
    @SerializedName("desc")
    val desc: String? = "",
    @SerializedName("img")
    val img: String? = "",
    @SerializedName("title")
    val title: String? = ""
):Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readParcelable(Button::class.java.classLoader),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(button, flags)
        parcel.writeString(desc)
        parcel.writeString(img)
        parcel.writeString(title)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Box> {
        override fun createFromParcel(parcel: Parcel): Box {
            return Box(parcel)
        }

        override fun newArray(size: Int): Array<Box?> {
            return arrayOfNulls(size)
        }
    }
}