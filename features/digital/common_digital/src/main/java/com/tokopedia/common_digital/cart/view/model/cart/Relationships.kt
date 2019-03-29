package com.tokopedia.common_digital.cart.view.model.cart

import android.os.Parcel
import android.os.Parcelable

/**
 * @author anggaprasetiyo on 3/2/17.
 */

class Relationships : Parcelable {
    var relationProduct: Relation? = null
    var relationCategory: Relation? = null
    var relationOperator: Relation? = null


    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(this.relationProduct, flags)
        dest.writeParcelable(this.relationCategory, flags)
        dest.writeParcelable(this.relationOperator, flags)
    }

    constructor() {}

    protected constructor(`in`: Parcel) {
        this.relationProduct = `in`.readParcelable(Relation::class.java.classLoader)
        this.relationCategory = `in`.readParcelable(Relation::class.java.classLoader)
        this.relationOperator = `in`.readParcelable(Relation::class.java.classLoader)
    }

    companion object CREATOR : Parcelable.Creator<Relationships> {
        override fun createFromParcel(source: Parcel): Relationships {
            return Relationships(source)
        }

        override fun newArray(size: Int): Array<Relationships?> {
            return arrayOfNulls(size)
        }
    }
}
