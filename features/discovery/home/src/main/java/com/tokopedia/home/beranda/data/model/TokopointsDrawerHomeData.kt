package com.tokopedia.home.beranda.data.model

import android.os.Parcel
import android.os.Parcelable

data class TokopointsDrawerHomeData (
    val tokopointsDrawer: TokopointsDrawer = TokopointsDrawer()
): Parcelable {
    constructor(parcel: Parcel) : this(parcel.readParcelable<TokopointsDrawer>(TokopointsDrawer::class.java.classLoader))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(tokopointsDrawer, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TokopointsDrawerHomeData> {
        override fun createFromParcel(parcel: Parcel): TokopointsDrawerHomeData {
            return TokopointsDrawerHomeData(parcel)
        }

        override fun newArray(size: Int): Array<TokopointsDrawerHomeData?> {
            return arrayOfNulls(size)
        }
    }
}