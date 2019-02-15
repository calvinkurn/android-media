package com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom

import android.os.Parcel
import android.os.Parcelable

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory.GroupChatTypeFactory
import com.tokopedia.groupchat.vote.view.model.VoteInfoViewModel

/**
 * @author by nisie on 2/27/18.
 */

class VoteAnnouncementViewModel : BaseChatViewModel, Visitable<GroupChatTypeFactory>, Parcelable {

    var voteType: String? = null
        private set
    var voteInfoViewModel: VoteInfoViewModel? = null
        private set


    constructor(message: String, voteType: String, createdAt: Long,
                updatedAt: Long, messageId: String, senderId: String,
                senderName: String, senderIconUrl: String,
                isInfluencer: Boolean, isAdministrator: Boolean,
                voteInfoViewModel: VoteInfoViewModel) : super(message, createdAt, updatedAt, messageId, senderId, senderName, senderIconUrl,
            isInfluencer, isAdministrator) {
        this.voteType = voteType
        this.voteInfoViewModel = voteInfoViewModel
    }

    override fun type(typeFactory: GroupChatTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.voteType)
        dest.writeParcelable(this.voteInfoViewModel, flags)
    }

    protected constructor(`in`: Parcel) : super(`in`) {
        this.voteType = `in`.readString()
        this.voteInfoViewModel = `in`.readParcelable(VoteInfoViewModel::class.java.classLoader)
    }

    companion object {

        const val POLLING_START = "polling_start"
        const val POLLING_FINISHED = "polling_finish"
        const val POLLING_END = "polling_end"
        const val POLLING_CANCEL = "polling_cancel"
        const val POLLING_UPDATE = "polling_update"

        @JvmField
        val CREATOR: Parcelable.Creator<VoteAnnouncementViewModel> = object : Parcelable.Creator<VoteAnnouncementViewModel> {
            override fun createFromParcel(source: Parcel): VoteAnnouncementViewModel {
                return VoteAnnouncementViewModel(source)
            }

            override fun newArray(size: Int): Array<VoteAnnouncementViewModel?> {
                return arrayOfNulls(size)
            }
        }
    }
}
