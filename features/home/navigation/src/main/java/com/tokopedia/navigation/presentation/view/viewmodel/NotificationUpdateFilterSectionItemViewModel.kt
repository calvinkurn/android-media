package com.tokopedia.navigation.presentation.view.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.navigation.presentation.adapter.typefactory.NotificationUpdateFilterSectionTypeFactory

class NotificationUpdateFilterSectionItemViewModel(
        var text: String = "",
        var id: String = "",
        var key: String = "",
        var selected: Boolean = false
) : Visitable<NotificationUpdateFilterSectionTypeFactory>, Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt() != 0
    )

    override fun type(typeFactory: NotificationUpdateFilterSectionTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(text)
        parcel.writeString(id)
        parcel.writeString(key)
        parcel.writeInt(if (selected) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NotificationUpdateFilterSectionItemViewModel> {
        override fun createFromParcel(parcel: Parcel): NotificationUpdateFilterSectionItemViewModel {
            return NotificationUpdateFilterSectionItemViewModel(parcel)
        }

        override fun newArray(size: Int): Array<NotificationUpdateFilterSectionItemViewModel?> {
            return arrayOfNulls(size)
        }
    }

}
