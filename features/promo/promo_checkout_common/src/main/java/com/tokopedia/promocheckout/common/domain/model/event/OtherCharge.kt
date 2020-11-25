package com.tokopedia.promocheckout.common.domain.model.event

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class OtherCharge(
        @SerializedName("conv_fee")
        @Expose
        val convFee: Int = 0
) : Parcelable {
        constructor(parcel: Parcel) : this(parcel.readInt()) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeInt(convFee)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<OtherCharge> {
                override fun createFromParcel(parcel: Parcel): OtherCharge {
                        return OtherCharge(parcel)
                }

                override fun newArray(size: Int): Array<OtherCharge?> {
                        return arrayOfNulls(size)
                }
        }
}