package com.tokopedia.chat_common.view.viewmodel

import android.os.Parcel
import android.os.Parcelable

class ChatRoomHeaderViewModel() : Parcelable{

    var name: String? = null
    var label: String? = null
    var senderId: String? = null
    var role: String? = null
    var mode: Int? = 0
    var keyword: String? = null
    var image: String? = null

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
        label = parcel.readString()
        senderId = parcel.readString()
        role = parcel.readString()
        mode = parcel.readValue(Int::class.java.classLoader) as? Int
        keyword = parcel.readString()
        image = parcel.readString()
    }


    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(name)
        dest?.writeString(label)
        dest?.writeString(senderId)
        dest?.writeString(role)
        dest?.writeValue(mode)
        dest?.writeString(keyword)
        dest?.writeString(image)
    }

    override fun describeContents(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object CREATOR : Parcelable.Creator<ChatRoomHeaderViewModel> {
        override fun createFromParcel(parcel: Parcel): ChatRoomHeaderViewModel {
            return ChatRoomHeaderViewModel(parcel)
        }

        override fun newArray(size: Int): Array<ChatRoomHeaderViewModel?> {
            return arrayOfNulls(size)
        }
    }


}
