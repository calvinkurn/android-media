package com.tokopedia.common_digital.product.presentation.model

import android.os.Parcel
import android.os.Parcelable

/**
 * @author anggaprasetiyo on 5/3/17.
 */
class Promo(var bonusText: String? = null,
            var id: String? = null,
            var newPrice: String? = null,
            var newPricePlain: Long = 0,
            var tag: String? = null,
            var terms: String? = null,
            var valueText: String? = null) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readLong(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(bonusText)
        parcel.writeString(id)
        parcel.writeString(newPrice)
        parcel.writeLong(newPricePlain)
        parcel.writeString(tag)
        parcel.writeString(terms)
        parcel.writeString(valueText)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Promo> {
        override fun createFromParcel(parcel: Parcel): Promo {
            return Promo(parcel)
        }

        override fun newArray(size: Int): Array<Promo?> {
            return arrayOfNulls(size)
        }
    }

}
