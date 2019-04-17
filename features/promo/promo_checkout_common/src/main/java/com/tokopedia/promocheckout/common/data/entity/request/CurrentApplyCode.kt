package com.tokopedia.promocheckout.common.data.entity.request

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 16/04/19.
 */
data class CurrentApplyCode (
        @SerializedName("codes")
        var codes: String? = null,

        @SerializedName("type")
        var type: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString())

    fun CurrentApplyCode(codes: String, type: String) {
        this.codes = codes
        this.type = type
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(codes)
        parcel.writeString(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CurrentApplyCode> {
        override fun createFromParcel(parcel: Parcel): CurrentApplyCode {
            return CurrentApplyCode(parcel)
        }

        override fun newArray(size: Int): Array<CurrentApplyCode?> {
            return arrayOfNulls(size)
        }
    }
}