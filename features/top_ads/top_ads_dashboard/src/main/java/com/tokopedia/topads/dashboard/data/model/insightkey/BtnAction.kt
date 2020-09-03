package com.tokopedia.topads.dashboard.data.model.insightkey


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class BtnAction(
    @SerializedName("insight")
    val insight: String? = ""
):Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(insight)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BtnAction> {
        override fun createFromParcel(parcel: Parcel): BtnAction {
            return BtnAction(parcel)
        }

        override fun newArray(size: Int): Array<BtnAction?> {
            return arrayOfNulls(size)
        }
    }
}