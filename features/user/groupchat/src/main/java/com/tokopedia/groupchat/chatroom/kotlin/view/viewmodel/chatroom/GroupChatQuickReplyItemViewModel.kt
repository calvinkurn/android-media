package com.tokopedia.groupchat.chatroom.kotlin.view.viewmodel.chatroom

import android.os.Parcel
import android.os.Parcelable

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.groupchat.chatroom.kotlin.view.adapter.chatroom.typefactory.QuickReplyTypeFactory

/**
 * @author by StevenFredian on 15/05/18.
 */

class GroupChatQuickReplyItemViewModel : Visitable<QuickReplyTypeFactory>, Parcelable {

    var id: String? = null
    var text: String? = null

    constructor(id: String, text: String) {
        this.id = id
        this.text = text
    }

    protected constructor(`in`: Parcel) {
        id = `in`.readString()
        text = `in`.readString()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(id)
        dest.writeString(text)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun type(typeFactory: QuickReplyTypeFactory): Int {
        return typeFactory.type(this)
    }

    companion object {
        val TYPE = "group chat"

        @JvmField
        val CREATOR: Parcelable.Creator<GroupChatQuickReplyItemViewModel> = object : Parcelable.Creator<GroupChatQuickReplyItemViewModel> {
            override fun createFromParcel(`in`: Parcel): GroupChatQuickReplyItemViewModel {
                return GroupChatQuickReplyItemViewModel(`in`)
            }

            override fun newArray(size: Int): Array<GroupChatQuickReplyItemViewModel?> {
                return arrayOfNulls(size)
            }
        }
    }
}
