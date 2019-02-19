package com.tokopedia.chat_common.view.viewmodel

import android.os.Parcel
import android.os.Parcelable

/**
 * shopId always f
 */
data class ChatRoomHeaderViewModel(
        var name: String = "",
        var label: String = "",
        var senderId: String = "",
        var role: String = "",
        var mode: Int = 1,
        var keyword: String = "",
        var image: String = "",
        var lastTimeOnline : Long = 0,
        var isOnline : Boolean = false,
        var shopId : Int = 0) : Parcelable {

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
        label = parcel.readString()
        senderId = parcel.readString()
        role = parcel.readString()
        mode = parcel.readValue(Int::class.java.classLoader) as Int
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
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ChatRoomHeaderViewModel> {

        override fun createFromParcel(parcel: Parcel): ChatRoomHeaderViewModel {
            return ChatRoomHeaderViewModel(parcel)
        }

        override fun newArray(size: Int): Array<ChatRoomHeaderViewModel?> {
            return arrayOfNulls(size)
        }
    }

    object Companion {
        const val MODE_DEFAULT_GET_CHAT: Int = 1
        const val ROLE_USER: String = "user"
        const val ROLE_SHOP: String = "shop"
        const val ROLE_OFFICIAL: String = "administrator"

    }


}
