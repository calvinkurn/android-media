package com.tokopedia.navigation.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.navigation.domain.pojo.Paging
import com.tokopedia.navigation.domain.pojo.UserInfo

class TransactionNotification() : Parcelable {

    var paging: Paging = Paging()
    var list: List<TransactionItemNotification> = arrayListOf()
    var userInfo: UserInfo = UserInfo()

    constructor(paging: Paging, list: List<TransactionItemNotification>, userInfo: UserInfo) : this() {
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

    companion object CREATOR : Parcelable.Creator<TransactionNotification> {
        override fun createFromParcel(parcel: Parcel): TransactionNotification {
            return TransactionNotification(parcel)
        }

        override fun newArray(size: Int): Array<TransactionNotification?> {
            return arrayOfNulls(size)
        }
    }

}
