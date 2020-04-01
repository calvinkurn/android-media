package com.tokopedia.notifcenter.data.viewbean

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.notifcenter.presentation.adapter.typefactory.filter.NotificationUpdateFilterSectionTypeFactory

class NotificationUpdateFilterSectionViewBean(
        var text: String = "",
        var id: String = "",
        var key: String = "",
        var selected: Boolean = false
) : Visitable<NotificationUpdateFilterSectionTypeFactory>, Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString()?: "",
            parcel.readString()?: "",
            parcel.readString()?: "",
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

    companion object CREATOR : Parcelable.Creator<NotificationUpdateFilterSectionViewBean> {
        override fun createFromParcel(parcel: Parcel): NotificationUpdateFilterSectionViewBean {
            return NotificationUpdateFilterSectionViewBean(parcel)
        }

        override fun newArray(size: Int): Array<NotificationUpdateFilterSectionViewBean?> {
            return arrayOfNulls(size)
        }
    }

}
