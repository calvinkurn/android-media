package com.tokopedia.navigation.presentation.view.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.navigation.presentation.adapter.typefactory.NotificationUpdateTypeFactory

class NotificationUpdateItemViewModel(
        var notificationId: String = "",
        var isRead: Boolean = false,
        var iconUrl: String? = "",
        var contentUrl: String = "",
        var time: String = "",
        var label: Int = 1,
        var title: String = "",
        var sectionTitle: String = "",
        var body: String = "",
        var templateKey: String = "",
        var appLink: String = ""
) : Visitable<NotificationUpdateTypeFactory>, Parcelable {

    override fun type(typeFactory: NotificationUpdateTypeFactory): Int {
        return typeFactory.type(this)
    }

    constructor(`in`: Parcel) : this() {
        notificationId = `in`.readString()
        isRead = `in`.readInt() != 0
        iconUrl = `in`.readString()
        contentUrl = `in`.readString()
        time = `in`.readString()
        label = `in`.readInt()
        title = `in`.readString()
        body = `in`.readString()
        templateKey = `in`.readString()
        appLink = `in`.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(notificationId)
        parcel.writeInt(if (isRead) 1 else 0)
        parcel.writeString(iconUrl)
        parcel.writeString(contentUrl)
        parcel.writeString(time)
        parcel.writeInt(label)
        parcel.writeString(title)
        parcel.writeString(body)
        parcel.writeString(templateKey)
        parcel.writeString(appLink)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {

        var BUYER_TYPE = 1
        var SELLER_TYPE = 2

        @JvmField
        val CREATOR: Parcelable.Creator<NotificationUpdateItemViewModel> = object : Parcelable.Creator<NotificationUpdateItemViewModel> {
            override fun createFromParcel(`in`: Parcel): NotificationUpdateItemViewModel {
                return NotificationUpdateItemViewModel(`in`)
            }

            override fun newArray(size: Int): Array<NotificationUpdateItemViewModel?> {
                return arrayOfNulls(size)
            }
        }
    }

}
