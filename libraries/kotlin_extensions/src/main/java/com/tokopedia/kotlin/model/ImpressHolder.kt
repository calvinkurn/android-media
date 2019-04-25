package com.tokopedia.kotlin.model

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.Expose

/**
 * Author errysuprayogi on 29,January,2019
 */
open class ImpressHolder : Parcelable {

    @Expose(serialize = false, deserialize = false)
    var isInvoke: Boolean = false
        private set

    constructor() {}

    operator fun invoke() {
        this.isInvoke = true
    }

    protected constructor(`in`: Parcel) {
        isInvoke = `in`.readByte().toInt() != 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeByte((if (isInvoke) 1 else 0).toByte())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ImpressHolder> = object : Parcelable.Creator<ImpressHolder> {
            override fun createFromParcel(`in`: Parcel): ImpressHolder {
                return ImpressHolder(`in`)
            }

            override fun newArray(size: Int): Array<ImpressHolder?> {
                return arrayOfNulls(size)
            }
        }
    }

}
