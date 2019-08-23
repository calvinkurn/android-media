package com.tokopedia.groupchat.room.view.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory.DynamicButtonTypeFactory

/**
 * @author : Steven 24/05/19
 */
class InteractiveButton(
        var isEnabled: Boolean = false,
        var balloonList: ArrayList<String> = arrayListOf<String>()
) : BaseDynamicButton() {

    override fun type(typeFactory: DynamicButtonTypeFactory): Int {
        return typeFactory.type(this)
    }

    constructor(parcel: Parcel) : this() {
        this.isEnabled = parcel.readByte() != 0.toByte()
        this.balloonList = java.util.ArrayList()
        parcel.readList(this.balloonList, String::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (isEnabled) 1 else 0)
        parcel.writeList(balloonList)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<InteractiveButton> {
        override fun createFromParcel(parcel: Parcel): InteractiveButton {
            return InteractiveButton(parcel)
        }

        override fun newArray(size: Int): Array<InteractiveButton?> {
            return arrayOfNulls(size)
        }
    }

}