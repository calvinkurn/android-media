package com.tokopedia.notifcenter.data.model

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.notifcenter.data.viewbean.NotificationUpdateFilterSectionViewBean

class NotificationFilterSection(
        var filterType: String = "",
        var title: String = "",
        var list: List<NotificationUpdateFilterSectionViewBean> = listOf()
): Parcelable {

    enum class FilterType(val type: String) {
        TYPE_ID("typeId"),
        TAG_ID("tagId")
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

    companion object CREATOR : Parcelable.Creator<NotificationFilterSection> {
        override fun createFromParcel(parcel: Parcel): NotificationFilterSection {
            return NotificationFilterSection(parcel)
        }

        override fun newArray(size: Int): Array<NotificationFilterSection?> {
            return arrayOfNulls(size)
        }
    }


}