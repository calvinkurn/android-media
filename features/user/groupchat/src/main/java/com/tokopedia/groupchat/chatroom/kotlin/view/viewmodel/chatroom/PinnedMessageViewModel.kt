package com.tokopedia.groupchat.chatroom.kotlin.view.viewmodel.chatroom

import android.os.Parcel
import android.os.Parcelable

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.groupchat.chatroom.kotlin.view.adapter.chatroom.typefactory.GroupChatTypeFactory

/**
 * @author by steven on 5/3/18.
 */

class PinnedMessageViewModel : Visitable<GroupChatTypeFactory>, Parcelable {

    var message: String
    var title: String
    var imageUrl: String
    var thumbnail: String

    constructor(message: String, title: String, imageUrl: String, thumbnail: String) {
        this.message = message
        this.title = title
        this.imageUrl = imageUrl
        this.thumbnail = thumbnail
    }

    override fun type(typeFactory: GroupChatTypeFactory): Int {
        return 0
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.message)
        dest.writeString(this.title)
        dest.writeString(this.imageUrl)
        dest.writeString(this.thumbnail)
    }

    protected constructor(`in`: Parcel) {
        this.message = `in`.readString()
        this.title = `in`.readString()
        this.imageUrl = `in`.readString()
        this.thumbnail = `in`.readString()
    }

    companion object {

        val TYPE = "pinned_message"

        @JvmField
        val CREATOR: Parcelable.Creator<PinnedMessageViewModel> = object : Parcelable.Creator<PinnedMessageViewModel> {
            override fun createFromParcel(source: Parcel): PinnedMessageViewModel {
                return PinnedMessageViewModel(source)
            }

            override fun newArray(size: Int): Array<PinnedMessageViewModel?> {
                return arrayOfNulls(size)
            }
        }
    }
}
