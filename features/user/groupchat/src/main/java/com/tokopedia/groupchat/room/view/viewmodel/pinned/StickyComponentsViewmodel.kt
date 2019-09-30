package com.tokopedia.groupchat.room.view.viewmodel.pinned

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable

class StickyComponentsViewModel(var list :List<StickyComponentViewModel>) : Visitable<Any>, Parcelable {

    constructor(parcel: Parcel) : this(arrayListOf()) {
        this.list = java.util.ArrayList()
        parcel.readList(this.list, StickyComponentViewModel::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeList(list)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StickyComponentViewModel> {

        const val TYPE = "sticky_components"

        override fun createFromParcel(parcel: Parcel): StickyComponentViewModel {
            return StickyComponentViewModel(parcel)
        }

        override fun newArray(size: Int): Array<StickyComponentViewModel?> {
            return arrayOfNulls(size)
        }
    }

    override fun type(typeFactory: Any?): Int {
        return 0
    }
}