package com.tokopedia.common_digital.cart.view.model.cart

import android.os.Parcel
import android.os.Parcelable

/**
 * @author anggaprasetiyo on 3/2/17.
 */

class RelationData : Parcelable {
    var type: String? = null
    var id: String? = null


    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.type)
        dest.writeString(this.id)
    }

    constructor() {}

    protected constructor(`in`: Parcel) {
        this.type = `in`.readString()
        this.id = `in`.readString()
    }

    companion object CREATOR : Parcelable.Creator<RelationData> {
        override fun createFromParcel(source: Parcel): RelationData {
            return RelationData(source)
        }

        override fun newArray(size: Int): Array<RelationData?> {
            return arrayOfNulls(size)
        }
    }
}
