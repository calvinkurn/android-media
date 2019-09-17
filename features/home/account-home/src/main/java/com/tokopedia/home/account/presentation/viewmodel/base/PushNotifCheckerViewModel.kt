package com.tokopedia.home.account.presentation.viewmodel.base

import android.os.Parcel
import android.os.Parcelable

class PushNotifCheckerViewModel(): Parcelable {
    private val items: List<ParcelableViewModel<String>> = ArrayList()

    constructor(parcel: Parcel) : this() { }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeTypedList(this.items)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PushNotifCheckerViewModel> {
        override fun createFromParcel(parcel: Parcel): PushNotifCheckerViewModel {
            return PushNotifCheckerViewModel(parcel)
        }

        override fun newArray(size: Int): Array<PushNotifCheckerViewModel?> {
            return arrayOfNulls(size)
        }
    }

}