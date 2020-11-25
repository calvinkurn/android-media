package com.tokopedia.withdraw.auto_withdrawal.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class GetInfoAutoWDResponse(
        @SerializedName("GetInfoAutoWD")
        val getInfoAutoWD: GetInfoAutoWD
)

data class GetInfoAutoWD(
        @SerializedName("code") val code: Int = 0,
        @SerializedName("data") val data: ArrayList<InfoAutoWDData>,
        @SerializedName("message") val message: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.createTypedArrayList(InfoAutoWDData) ?: arrayListOf(),
            parcel.readString() ?: "") {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(code)
        parcel.writeString(message)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GetInfoAutoWD> {
        override fun createFromParcel(parcel: Parcel): GetInfoAutoWD {
            return GetInfoAutoWD(parcel)
        }

        override fun newArray(size: Int): Array<GetInfoAutoWD?> {
            return arrayOfNulls(size)
        }
    }
}

data class InfoAutoWDData(
        @SerializedName("title") val title: String,
        @SerializedName("desc") val description: String,
        @SerializedName("icon") val icon: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString()?:"",
            parcel.readString()?:"",
            parcel.readString()?:"") {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(icon)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<InfoAutoWDData> {
        override fun createFromParcel(parcel: Parcel): InfoAutoWDData {
            return InfoAutoWDData(parcel)
        }

        override fun newArray(size: Int): Array<InfoAutoWDData?> {
            return arrayOfNulls(size)
        }
    }
}
