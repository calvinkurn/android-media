package com.tokopedia.browse.homepage.presentation.model

import android.os.Parcel
import android.os.Parcelable

/**
 * @author by furqan on 07/09/18.
 */

class IndexPositionModel(var indexPositionInTab: Int = 0,
                         var indexPositionInList: Int = 0) :
        Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(indexPositionInTab)
        parcel.writeInt(indexPositionInList)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<IndexPositionModel> {
        override fun createFromParcel(parcel: Parcel): IndexPositionModel {
            return IndexPositionModel(parcel)
        }

        override fun newArray(size: Int): Array<IndexPositionModel?> {
            return arrayOfNulls(size)
        }
    }
}
