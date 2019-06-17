package com.tokopedia.common_digital.product.presentation.model

import android.os.Parcel
import android.os.Parcelable

/**
 * @author anggaprasetiyo on 5/3/17.
 */
class Validation(
        var regex: String? = null,
        var error: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(regex)
        parcel.writeString(error)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Validation> {
        override fun createFromParcel(parcel: Parcel): Validation {
            return Validation(parcel)
        }

        override fun newArray(size: Int): Array<Validation?> {
            return arrayOfNulls(size)
        }
    }
}
