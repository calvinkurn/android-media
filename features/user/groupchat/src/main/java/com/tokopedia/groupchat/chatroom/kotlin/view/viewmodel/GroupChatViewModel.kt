package com.tokopedia.groupchat.chatroom.kotlin.view.viewmodel

import android.os.Parcel
import android.os.Parcelable

/**
 * @author by nisie on 2/14/18.
 */

class GroupChatViewModel : Parcelable {

    var channelUuid: String? = null
        private set
    var channelInfoViewModel: ChannelInfoViewModel? = null
        private set
    var timeStampAfterPause: Long = 0
    var timeStampAfterResume: Long = 0
    var channelPosition: Int = 0
        private set

    var totalView: String?
        get() = if (channelInfoViewModel != null) channelInfoViewModel!!.totalView else "0"
        set(totalView) {
            if (channelInfoViewModel != null) {
                this.channelInfoViewModel!!.totalView = totalView
            }
        }

    val channelName: String?
        get() = if (channelInfoViewModel != null) channelInfoViewModel!!.title else ""


    val channelUrl: String?
        get() = if (channelInfoViewModel != null) channelInfoViewModel!!.channelUrl else ""

    val pollId: String
        get() = if (channelInfoViewModel != null && channelInfoViewModel!!.voteInfoViewModel != null) {
            this.channelInfoViewModel!!.voteInfoViewModel!!.pollId
        } else {
            ""
        }

    constructor(channelUuid: String, channelPosition: Int) {
        this.channelUuid = channelUuid
        this.channelInfoViewModel = null
        this.timeStampAfterPause = 0
        this.timeStampAfterResume = 0
        this.channelPosition = channelPosition
    }

    protected constructor(`in`: Parcel) {
        channelUuid = `in`.readString()
        channelInfoViewModel = `in`.readParcelable(ChannelInfoViewModel::class.java.classLoader)
        timeStampAfterPause = `in`.readLong()
        timeStampAfterResume = `in`.readLong()
        channelPosition = `in`.readInt()
    }

    fun setChannelInfo(channelInfoViewModel: ChannelInfoViewModel) {
        this.channelInfoViewModel = channelInfoViewModel
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(channelUuid)
        dest.writeParcelable(channelInfoViewModel, flags)
        dest.writeLong(timeStampAfterPause)
        dest.writeLong(timeStampAfterResume)
        dest.writeInt(channelPosition)
    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<GroupChatViewModel> = object : Parcelable.Creator<GroupChatViewModel> {
            override fun createFromParcel(`in`: Parcel): GroupChatViewModel {
                return GroupChatViewModel(`in`)
            }

            override fun newArray(size: Int): Array<GroupChatViewModel?> {
                return arrayOfNulls(size)
            }
        }
    }
}
