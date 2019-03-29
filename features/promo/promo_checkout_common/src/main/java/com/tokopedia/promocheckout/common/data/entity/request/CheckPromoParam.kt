package com.tokopedia.promocheckout.common.data.entity.request

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 19/03/19.
 */

data class CheckPromoParam(
        @SerializedName("promo")
        var promo: Promo? = null
) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readParcelable(Promo::class.java.classLoader) ?: null
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeParcelable(promo, flags)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<CheckPromoParam> {
                override fun createFromParcel(parcel: Parcel): CheckPromoParam {
                        return CheckPromoParam(parcel)
                }

                override fun newArray(size: Int): Array<CheckPromoParam?> {
                        return arrayOfNulls(size)
                }
        }
}