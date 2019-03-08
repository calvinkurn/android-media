package com.tokopedia.common_digital.product.presentation.model

import android.os.Parcel
import android.os.Parcelable

/**
 * @author anggaprasetiyo on 5/3/17.
 */
class Promo : Parcelable {

    var bonusText: String? = null
    var id: String? = null
    var newPrice: String? = null
    var newPricePlain: Long = 0
    var tag: String? = null
    var terms: String? = null
    var valueText: String? = null


    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.bonusText)
        dest.writeString(this.id)
        dest.writeString(this.newPrice)
        dest.writeLong(this.newPricePlain)
        dest.writeString(this.tag)
        dest.writeString(this.terms)
        dest.writeString(this.valueText)
    }

    constructor() {}

    protected constructor(`in`: Parcel) {
        this.bonusText = `in`.readString()
        this.id = `in`.readString()
        this.newPrice = `in`.readString()
        this.newPricePlain = `in`.readLong()
        this.tag = `in`.readString()
        this.terms = `in`.readString()
        this.valueText = `in`.readString()
    }

    companion object CREATOR : Parcelable.Creator<Promo> {
        override fun createFromParcel(source: Parcel): Promo {
            return Promo(source)
        }

        override fun newArray(size: Int): Array<Promo?> {
            return arrayOfNulls(size)
        }
    }
}
