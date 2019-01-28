package com.tokopedia.topads.common.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FreeDeposit(
        @SerializedName("deposit_id")
        @Expose
        val depositId: Int = 0,
        @SerializedName("nominal")
        @Expose
        val nominal: Double = 0.00,
        @SerializedName("nominal_fmt")
        @Expose
        val nominalFmt: String = "",
        @SerializedName("remaining_days")
        @Expose
        val remainingDays: Int = 0,
        @SerializedName("status")
        @Expose
        val status: Int = 0,
        @SerializedName("usage")
        @Expose
        val usage: Double = 0.00,
        @SerializedName("usage_fmt")
        @Expose
        val usageFmt: String = ""
): Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readInt(),
                parcel.readDouble(),
                parcel.readString(),
                parcel.readInt(),
                parcel.readInt(),
                parcel.readDouble(),
                parcel.readString()) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeInt(depositId)
                parcel.writeDouble(nominal)
                parcel.writeString(nominalFmt)
                parcel.writeInt(remainingDays)
                parcel.writeInt(status)
                parcel.writeDouble(usage)
                parcel.writeString(usageFmt)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<FreeDeposit> {
                override fun createFromParcel(parcel: Parcel): FreeDeposit {
                        return FreeDeposit(parcel)
                }

                override fun newArray(size: Int): Array<FreeDeposit?> {
                        return arrayOfNulls(size)
                }
        }
}