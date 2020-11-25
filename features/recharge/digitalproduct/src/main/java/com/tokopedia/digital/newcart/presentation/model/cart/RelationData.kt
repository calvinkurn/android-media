package com.tokopedia.digital.newcart.presentation.model.cart

import android.os.Parcel
import android.os.Parcelable

/**
 * @author anggaprasetiyo on 3/2/17.
 */

class RelationData(
        var type: String? = null,
        var id: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type)
        parcel.writeString(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RelationData> {
        override fun createFromParcel(parcel: Parcel): RelationData {
            return RelationData(parcel)
        }

        override fun newArray(size: Int): Array<RelationData?> {
            return arrayOfNulls(size)
        }
    }

}
