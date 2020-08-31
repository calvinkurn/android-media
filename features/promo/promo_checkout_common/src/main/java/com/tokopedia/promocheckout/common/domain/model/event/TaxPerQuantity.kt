package com.tokopedia.promocheckout.common.domain.model.event

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class TaxPerQuantity(
        @SerializedName("entertainment")
        @Expose
        val entertainment: Int = 0,
        @SerializedName("service")
        @Expose
        val service: Int = 0
) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readInt(),
                parcel.readInt()) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeInt(entertainment)
                parcel.writeInt(service)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<TaxPerQuantity> {
                override fun createFromParcel(parcel: Parcel): TaxPerQuantity {
                        return TaxPerQuantity(parcel)
                }

                override fun newArray(size: Int): Array<TaxPerQuantity?> {
                        return arrayOfNulls(size)
                }
        }
}
