package com.tokopedia.notifcenter.data.model

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.notifcenter.data.viewbean.NotificationItemViewBean
import com.tokopedia.notifcenter.data.entity.Paging
import com.tokopedia.notifcenter.data.entity.UserInfo
import java.util.*

open class NotificationViewData() : Parcelable {

    var paging: Paging = Paging()
    var list: List<NotificationItemViewBean> = arrayListOf()
    var userInfo: UserInfo = UserInfo()

    constructor(paging: Paging, list: List<NotificationItemViewBean>, userInfo: UserInfo) : this() {
        this.paging = paging
        this.list = list
        this.userInfo = userInfo
    }

    constructor(parcel: Parcel) : this() {
        this.list = ArrayList()
        parcel.readList(this.list, Visitable::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(list)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NotificationViewData> {
        override fun createFromParcel(parcel: Parcel): NotificationViewData {
            return NotificationViewData(parcel)
        }

        override fun newArray(size: Int): Array<NotificationViewData?> {
            return arrayOfNulls(size)
        }
    }

}
