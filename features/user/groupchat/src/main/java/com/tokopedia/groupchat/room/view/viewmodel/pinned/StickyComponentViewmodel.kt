package com.tokopedia.groupchat.room.view.viewmodel.pinned

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable

/**
 * @author by yfsx on 14/02/19.
 */
data class StickyComponentViewModel (val componentId : String = "",
                                     val componentType: String = "",
                                     val imageUrl: String = "",
                                     val title: String = "",
                                     val subtitle: String = "",
                                     val redirectUrl : String = "",
                                     val stickyTime : Int = 0) : Visitable<Any>, Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(componentId)
        parcel.writeString(componentType)
        parcel.writeString(imageUrl)
        parcel.writeString(title)
        parcel.writeString(subtitle)
        parcel.writeString(redirectUrl)
        parcel.writeInt(stickyTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StickyComponentViewModel> {

        const val TYPE = "STICKY_COMPONENT"
        const val TYPE_PRODUCT = "product"

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