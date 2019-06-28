package com.tokopedia.navigation.presentation.view.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.navigation.domain.pojo.Paging
import com.tokopedia.navigation.domain.pojo.UserInfo
import java.util.*

open class NotificationUpdateViewModel() : Parcelable{

    var paging: Paging = Paging()
    var list: List<NotificationUpdateItemViewModel> = arrayListOf()
    var userInfo: UserInfo = UserInfo()

    constructor(paging: Paging, list: List<NotificationUpdateItemViewModel>, userInfo: UserInfo) : this() {
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

    companion object CREATOR : Parcelable.Creator<NotificationUpdateViewModel> {
        override fun createFromParcel(parcel: Parcel): NotificationUpdateViewModel {
            return NotificationUpdateViewModel(parcel)
        }

        override fun newArray(size: Int): Array<NotificationUpdateViewModel?> {
            return arrayOfNulls(size)
        }
    }

}
