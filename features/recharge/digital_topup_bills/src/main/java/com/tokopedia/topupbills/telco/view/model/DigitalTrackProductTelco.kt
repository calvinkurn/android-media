package com.tokopedia.topupbills.telco.view.model

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.topupbills.telco.data.TelcoProduct

class DigitalTrackProductTelco constructor(
        val itemProduct: TelcoProduct,
        val position: Int) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readParcelable(TelcoProduct::class.java.classLoader),
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
        return this.itemProduct.id == other.itemProduct.id
    }

    override fun hashCode(): Int {
        return 31 * this.itemProduct.id.toInt()
    }

}