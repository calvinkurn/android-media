package com.tokopedia.groupchat.room.view.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory.DynamicButtonTypeFactory

/**
 * @author : Steven 24/05/19
 */
class DynamicButton(var buttonId : String = "",
                    var imageUrl: String = "",
                    var linkUrl: String = "",
                    var contentType: String = "",
                    var contentText: String = "",
                    var contentButtonText: String = "",
                    var contentLinkUrl: String = "",
                    var contentImageUrl: String = "",
                    var hasNotification: Boolean = false,
                    var tooltip: String = "",
                    var tooltipDuration: Int = 0,
                    var priority: Int = 0
) : BaseDynamicButton() {

    override fun type(typeFactory: DynamicButtonTypeFactory): Int {
        return typeFactory.type(this)
    }

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(buttonId)
        parcel.writeString(imageUrl)
        parcel.writeString(linkUrl)
        parcel.writeString(contentType)
        parcel.writeString(contentText)
        parcel.writeString(contentButtonText)
        parcel.writeString(contentLinkUrl)
        parcel.writeString(contentImageUrl)
        parcel.writeByte(if (hasNotification) 1 else 0)
        parcel.writeString(tooltip)
        parcel.writeInt(tooltipDuration)
        parcel.writeInt(priority)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DynamicButton> {
        override fun createFromParcel(parcel: Parcel): DynamicButton {
            return DynamicButton(parcel)
        }

        override fun newArray(size: Int): Array<DynamicButton?> {
            return arrayOfNulls(size)
        }
    }

}

