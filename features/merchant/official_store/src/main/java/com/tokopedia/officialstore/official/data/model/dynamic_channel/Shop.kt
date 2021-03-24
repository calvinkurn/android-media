package com.tokopedia.officialstore.official.data.model.dynamic_channel

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Created by Lukas on 3/24/21.
 */
data class Shop (
        @SerializedName("shopID")
        val shopId: Int = 0
) : Parcelable {
        constructor(parcel: Parcel) : this(parcel.readInt()) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeInt(shopId)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<Shop> {
                override fun createFromParcel(parcel: Parcel): Shop {
                        return Shop(parcel)
                }

                override fun newArray(size: Int): Array<Shop?> {
                        return arrayOfNulls(size)
                }
        }
}