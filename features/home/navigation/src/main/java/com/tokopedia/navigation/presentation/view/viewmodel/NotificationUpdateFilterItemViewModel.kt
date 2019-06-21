package com.tokopedia.navigation.presentation.view.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.navigation.presentation.adapter.typefactory.NotificationUpdateFilterTypeFactory

class NotificationUpdateFilterItemViewModel(
        var filterType: String = "",
        var title: String = "",
        var list: List<NotificationUpdateFilterSectionItemViewModel> = arrayListOf()
) : Visitable<NotificationUpdateFilterTypeFactory>, Parcelable {

    enum class FilterType(val type: String) {
        TYPE_ID("typeId"),
        TAG_ID("tagId")
    }

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.createTypedArrayList(NotificationUpdateFilterSectionItemViewModel.CREATOR))

    override fun type(typeFactory: NotificationUpdateFilterTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(filterType)
        parcel.writeString(title)
        parcel.writeTypedList(list)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NotificationUpdateFilterItemViewModel> {
        override fun createFromParcel(parcel: Parcel): NotificationUpdateFilterItemViewModel {
            return NotificationUpdateFilterItemViewModel(parcel)
        }

        override fun newArray(size: Int): Array<NotificationUpdateFilterItemViewModel?> {
            return arrayOfNulls(size)
        }
    }


}
