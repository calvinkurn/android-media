package com.tokopedia.topupbills.telco.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nabillasabbaha on 16/05/19.
 */
class TelcoProductPromo(
        @SerializedName("id")
        @Expose
        val id: Int = 0,
        @SerializedName("bonus_text")
        @Expose
        val bonusText: String = "",
        @SerializedName("new_price")
        @Expose
        val newPrice: String = "",
        @SerializedName("new_price_plain")
        @Expose
        val newPricePlain: Int = 0,
        @SerializedName("value_text")
        @Expose
        val valueText: String = "")
    : Parcelable {

        constructor(parcel: Parcel) : this(
                parcel.readInt(),
                parcel.readString(),
                parcel.readString(),
                parcel.readInt(),
                parcel.readString())

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeInt(id)
                parcel.writeString(bonusText)
                parcel.writeString(newPrice)
                parcel.writeInt(newPricePlain)
                parcel.writeString(valueText)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<TelcoProductPromo> {
                override fun createFromParcel(parcel: Parcel): TelcoProductPromo {
                        return TelcoProductPromo(parcel)
                }

                override fun newArray(size: Int): Array<TelcoProductPromo?> {
                        return arrayOfNulls(size)
                }
        }
}