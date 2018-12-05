package com.tokopedia.chat_common.view.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable

class ChatRoomViewModel() : Parcelable{

    var listChat: ArrayList<Visitable<*>>? = null

    constructor(parcel: Parcel) : this() {

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ChatRoomViewModel> {
        override fun createFromParcel(parcel: Parcel): ChatRoomViewModel {
            return ChatRoomViewModel(parcel)
        }

        override fun newArray(size: Int): Array<ChatRoomViewModel?> {
            return arrayOfNulls(size)
        }
    }
}
