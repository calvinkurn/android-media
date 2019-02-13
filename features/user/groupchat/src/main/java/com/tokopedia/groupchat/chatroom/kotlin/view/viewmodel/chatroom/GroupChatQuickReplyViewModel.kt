package com.tokopedia.groupchat.chatroom.kotlin.view.viewmodel.chatroom

import android.os.Parcel
import android.os.Parcelable

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.groupchat.chatroom.kotlin.view.adapter.chatroom.typefactory.QuickReplyTypeFactory

import java.util.ArrayList

/**
 * @author by StevenFredian on 15/05/18.
 */

class GroupChatQuickReplyViewModel : Visitable<QuickReplyTypeFactory>, Parcelable {

    var list: List<GroupChatQuickReplyItemViewModel>? = null

    override fun type(typeFactory: QuickReplyTypeFactory): Int {
        return 0
    }


    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeList(this.list)
    }

    constructor() {}

    protected constructor(`in`: Parcel) {
        this.list = ArrayList()
        `in`.readList(this.list, Visitable::class.java.classLoader)
    }

    companion object {
        val TYPE = "quick_reply"

        @JvmField
        val CREATOR: Parcelable.Creator<GroupChatQuickReplyViewModel> = object : Parcelable.Creator<GroupChatQuickReplyViewModel> {
            override fun createFromParcel(source: Parcel): GroupChatQuickReplyViewModel {
                return GroupChatQuickReplyViewModel(source)
            }

            override fun newArray(size: Int): Array<GroupChatQuickReplyViewModel?> {
                return arrayOfNulls(size)
            }
        }
    }
}
