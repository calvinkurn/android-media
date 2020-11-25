package com.tokopedia.tokopoints.view.model.rewardintro

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ResultStatus(

        @SerializedName("code")
        val code: String? = null,

        @SerializedName("message")
        val message: List<String?>? = null,

        @SerializedName("status")
        val status: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.createStringArrayList(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(code)
        parcel.writeStringList(message)
        parcel.writeString(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ResultStatus> {
        override fun createFromParcel(parcel: Parcel): ResultStatus {
            return ResultStatus(parcel)
        }

        override fun newArray(size: Int): Array<ResultStatus?> {
            return arrayOfNulls(size)
        }
    }
}