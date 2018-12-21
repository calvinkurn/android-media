package com.tokopedia.common_digital.product.presentation.model

import android.os.Parcel
import android.os.Parcelable

/**
 * @author anggaprasetiyo on 5/3/17.
 */
class Validation : Parcelable {

    var regex: String? = null
    var error: String? = null

    constructor(regex: String, error: String) {
        this.regex = regex
        this.error = error
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.regex)
        dest.writeString(this.error)
    }

    constructor() {}

    protected constructor(`in`: Parcel) {
        this.regex = `in`.readString()
        this.error = `in`.readString()
    }

    companion object CREATOR : Parcelable.Creator<Validation> {
        override fun createFromParcel(source: Parcel): Validation {
            return Validation(source)
        }

        override fun newArray(size: Int): Array<Validation?> {
            return arrayOfNulls(size)
        }
    }
}
