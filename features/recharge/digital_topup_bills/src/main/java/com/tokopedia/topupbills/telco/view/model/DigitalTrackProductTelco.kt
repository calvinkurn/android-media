package com.tokopedia.topupbills.telco.view.model

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.topupbills.telco.data.TelcoProductDataCollection

class DigitalTrackProductTelco constructor(
        val itemProduct: TelcoProductDataCollection,
        val position: Int) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readParcelable(TelcoProductDataCollection::class.java.classLoader),
            parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(itemProduct, flags)
        parcel.writeInt(position)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DigitalTrackProductTelco> {
        override fun createFromParcel(parcel: Parcel): DigitalTrackProductTelco {
            return DigitalTrackProductTelco(parcel)
        }

        override fun newArray(size: Int): Array<DigitalTrackProductTelco?> {
            return arrayOfNulls(size)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is DigitalTrackProductTelco) return false
        return this.itemProduct.product.id == other.itemProduct.product.id
    }

    override fun hashCode(): Int {
        return 31 * this.itemProduct.product.id.toInt()
    }

}