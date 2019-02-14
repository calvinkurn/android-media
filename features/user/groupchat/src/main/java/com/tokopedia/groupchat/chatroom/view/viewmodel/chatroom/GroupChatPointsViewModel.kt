package com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom

import android.os.Parcel
import android.os.Parcelable

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory.GroupChatTypeFactory

/**
 * @author by StevenFredian on 28/03/18.
 */

class GroupChatPointsViewModel : Parcelable, Visitable<GroupChatTypeFactory> {


    var image: String? = null
    var text: String? = null
    var span: String? = null
    var url: String? = null
    var type: String? = null

    constructor(text: String, url: String) {
        this.text = text
        this.url = url
    }

    constructor(text: String, url: String, type: String) {
        this.text = text
        this.url = url
        this.type = type
    }

    protected constructor(`in`: Parcel) {
        image = `in`.readString()
        text = `in`.readString()
        span = `in`.readString()
        url = `in`.readString()
        type = `in`.readString()
    }

    override fun type(typeFactory: GroupChatTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(image)
        dest.writeString(text)
        dest.writeString(span)
        dest.writeString(url)
        dest.writeString(type)
    }

    companion object {

        const val TYPE_POINTS = "1401"
        const val TYPE_LOYALTY = "1402"
        const val TYPE_COUPON = "1403"

        @JvmField
        val CREATOR: Parcelable.Creator<GroupChatPointsViewModel> = object : Parcelable.Creator<GroupChatPointsViewModel> {
            override fun createFromParcel(`in`: Parcel): GroupChatPointsViewModel {
                return GroupChatPointsViewModel(`in`)
            }

            override fun newArray(size: Int): Array<GroupChatPointsViewModel?> {
                return arrayOfNulls(size)
            }
        }
    }
}
