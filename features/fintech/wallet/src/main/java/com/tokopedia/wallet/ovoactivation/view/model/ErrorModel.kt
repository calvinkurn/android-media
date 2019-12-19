package com.tokopedia.wallet.ovoactivation.view.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by nabillasabbaha on 10/10/18.
 */
class ErrorModel(var message: String = "") : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(message)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ErrorModel> {
        override fun createFromParcel(parcel: Parcel): ErrorModel {
            return ErrorModel(parcel)
        }

        override fun newArray(size: Int): Array<ErrorModel?> {
            return arrayOfNulls(size)
        }
    }
}

