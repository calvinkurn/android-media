package com.tokopedia.common_digital.cart.view.model.cart

import android.os.Parcel
import android.os.Parcelable

/**
 * @author anggaprasetiyo on 3/2/17.
 */

class Relation : Parcelable {

    var data: RelationData? = null

    constructor(data: RelationData) {
        this.data = data
    }


    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(this.data, flags)
    }

    protected constructor(`in`: Parcel) {
        this.data = `in`.readParcelable(RelationData::class.java.classLoader)
    }

    companion object CREATOR : Parcelable.Creator<Relation> {
        override fun createFromParcel(source: Parcel): Relation {
            return Relation(source)
        }

        override fun newArray(size: Int): Array<Relation?> {
            return arrayOfNulls(size)
        }
    }
}
