package com.tokopedia.notifcenter.data.viewbean

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.notifcenter.presentation.adapter.typefactory.filter.NotificationUpdateFilterTypeFactory

open class NotificationUpdateFilterViewBean(
        var filterType: String = "",
        var title: String = "",
        var list: List<NotificationUpdateFilterSectionViewBean> = arrayListOf()
) : Visitable<NotificationUpdateFilterTypeFactory>, Parcelable {

    enum class FilterType(val type: String) {
        TYPE_ID("typeId"),
        TAG_ID("tagId")
    }

    override fun type(typeFactory: NotificationUpdateFilterTypeFactory?): Int {
        return -1
    }

    constructor(parcel: Parcel) : this(
            parcel.readString()?: "",
            parcel.readString()?: "",
            parcel.createTypedArrayList(NotificationUpdateFilterSectionViewBean.CREATOR)?: listOf())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(filterType)
        parcel.writeString(title)
        parcel.writeTypedList(list)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NotificationUpdateFilterViewBean> {
        override fun createFromParcel(parcel: Parcel): NotificationUpdateFilterViewBean {
            return NotificationUpdateFilterViewBean(parcel)
        }

        override fun newArray(size: Int): Array<NotificationUpdateFilterViewBean?> {
            return arrayOfNulls(size)
        }
    }


}
