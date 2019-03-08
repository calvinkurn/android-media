package com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom

import android.os.Parcel
import android.os.Parcelable

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory.GroupChatTypeFactory

/**
 * @author : Steven 06/11/18
 */
class ParticipantViewModel : Visitable<GroupChatTypeFactory>, Parcelable {

    var channelId: String
    var totalView: String


    constructor(`in`: Parcel) {
        channelId = `in`.readString()
        totalView = `in`.readString()
    }

    constructor(channelId: String, totalView: String) {
        this.channelId = channelId
        this.totalView = totalView
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(channelId)
        dest.writeString(totalView)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun type(typeFactory: GroupChatTypeFactory): Int {
        return 0
    }

    companion object {

        const val TYPE = "total_view"

        @JvmField
        val CREATOR: Parcelable.Creator<ParticipantViewModel> = object : Parcelable.Creator<ParticipantViewModel> {
            override fun createFromParcel(`in`: Parcel): ParticipantViewModel {
                return ParticipantViewModel(`in`)
            }

            override fun newArray(size: Int): Array<ParticipantViewModel?> {
                return arrayOfNulls(size)
            }
        }
    }
}
