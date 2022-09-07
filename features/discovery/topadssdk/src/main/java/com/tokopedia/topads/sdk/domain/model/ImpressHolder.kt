package com.tokopedia.topads.sdk.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose

open class ImpressHolder() : Parcelable {
    @Expose(serialize = false, deserialize = false)
    var isInvoke = false
        private set

    protected constructor(`in`: Parcel) : this() {
        isInvoke = `in`.readByte().toInt() != 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeByte((if (isInvoke) 1 else 0).toByte())
    }

    override fun describeContents(): Int {
        return 0
    }

    operator fun invoke() {
        isInvoke = true
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ImpressHolder> =
            object : Parcelable.Creator<ImpressHolder> {
                override fun createFromParcel(`in`: Parcel): ImpressHolder {
                    return ImpressHolder(`in`)
                }

                override fun newArray(size: Int): Array<ImpressHolder?> {
                    return arrayOfNulls(size)
                }
            }
    }
}