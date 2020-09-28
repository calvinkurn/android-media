package com.tokopedia.groupchat.room.view.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory.DynamicButtonTypeFactory
import kotlinx.android.parcel.Parcelize

/**
 * @author : Steven 24/05/19
 */
@Parcelize
class InteractiveButton(
        var isEnabled: Boolean = false,
        var balloonList: ArrayList<String> = arrayListOf<String>()
) : BaseDynamicButton() {

    override fun type(typeFactory: DynamicButtonTypeFactory): Int {
        return typeFactory.type(this)
    }

}