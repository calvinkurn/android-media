package com.tokopedia.groupchat.chatroom.kotlin.view.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.*
import com.tokopedia.groupchat.chatroom.view.viewmodel.interupt.OverlayViewModel
import com.tokopedia.groupchat.chatroom.domain.pojo.ExitMessage
import com.tokopedia.groupchat.chatroom.domain.pojo.channelinfo.SettingGroupChat
import com.tokopedia.groupchat.vote.view.model.VoteInfoViewModel

/**
 * @author by nisie on 2/22/18.
 */

class ChannelInfoViewModel : Parcelable {
    var channelId: String? = null
        private set
    var title: String? = null
        private set
    var channelUrl: String? = null
        private set
    var bannerUrl: String? = null
        private set
    var blurredBannerUrl: String? = null
        private set
    var adsImageUrl: String? = null
    var adsLink: String? = null
    var adsId: String? = null
    var adsName: String? = null
    var bannerName: String? = null
        private set
    var groupChatToken: String? = null
        private set
    var adminName: String? = null
        private set
    var image: String? = null
        private set
    var adminPicture: String? = null
        private set
    var description: String? = null
        private set
    var totalView: String? = null
        internal set
    var channelPartnerViewModels: List<ChannelPartnerViewModel>? = null
        private set
    var bannedMessage: String? = null
        private set
    var kickedMessage: String? = null
        private set
    var isFreeze: Boolean = false
        private set
    var videoId: String? = null
    var settingGroupChat: SettingGroupChat? = null
        private set
    var overlayViewModel: OverlayViewModel? = null
        private set

    var voteInfoViewModel: VoteInfoViewModel? = null

    var sprintSaleViewModel: SprintSaleViewModel? = null

    var groupChatPointsViewModel: GroupChatPointsViewModel? = null

    var pinnedMessageViewModel: PinnedMessageViewModel? = null

    var exitMessage: ExitMessage? = null
        private set

    var quickRepliesViewModel: List<GroupChatQuickReplyItemViewModel>? = null

    constructor(channelId: String, title: String, channelUrl: String, bannerUrl: String,
                blurredBannerUrl: String,
                adsImageUrl: String, adsLink: String, adsName: String, adsId: String,
                bannerName: String, groupChatToken: String, adminName: String, image: String,
                adminPicture: String, description: String, totalView: String,
                channelPartnerViewModels: List<ChannelPartnerViewModel>,
                voteInfoViewModel: VoteInfoViewModel?,
                sprintSaleViewModel: SprintSaleViewModel?,
                bannedMessage: String, kickedMessage: String, isFreeze: Boolean,
                pinnedMessageViewModel: PinnedMessageViewModel?,
                exitMessage: ExitMessage?,
                quickRepliesViewModel: List<GroupChatQuickReplyItemViewModel>,
                videoId: String, settingGroupChat: SettingGroupChat, overlayViewModel: OverlayViewModel) {
        this.channelId = channelId
        this.title = title
        this.channelUrl = channelUrl
        this.bannerUrl = bannerUrl
        this.blurredBannerUrl = blurredBannerUrl
        this.adsImageUrl = adsImageUrl
        this.adsLink = adsLink
        this.adsName = adsName
        this.adsId = adsId
        this.bannerName = bannerName
        this.groupChatToken = groupChatToken
        this.adminName = adminName
        this.image = image
        this.adminPicture = adminPicture
        this.description = description
        this.totalView = totalView
        this.channelPartnerViewModels = channelPartnerViewModels
        this.voteInfoViewModel = voteInfoViewModel
        this.sprintSaleViewModel = sprintSaleViewModel
        this.bannedMessage = bannedMessage
        this.kickedMessage = kickedMessage
        this.isFreeze = isFreeze
        this.pinnedMessageViewModel = pinnedMessageViewModel
        this.exitMessage = exitMessage
        this.quickRepliesViewModel = quickRepliesViewModel
        this.videoId = videoId
        this.settingGroupChat = settingGroupChat
        this.overlayViewModel = overlayViewModel
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.channelId)
        dest.writeString(this.title)
        dest.writeString(this.channelUrl)
        dest.writeString(this.bannerUrl)
        dest.writeString(this.blurredBannerUrl)
        dest.writeString(this.adsImageUrl)
        dest.writeString(this.adsLink)
        dest.writeString(this.adsId)
        dest.writeString(this.adsName)
        dest.writeString(this.bannerName)
        dest.writeString(this.groupChatToken)
        dest.writeString(this.adminName)
        dest.writeString(this.image)
        dest.writeString(this.adminPicture)
        dest.writeString(this.description)
        dest.writeString(this.totalView)
        dest.writeTypedList(this.channelPartnerViewModels)
        dest.writeString(this.bannedMessage)
        dest.writeString(this.kickedMessage)
        dest.writeByte(if (this.isFreeze) 1.toByte() else 0.toByte())
        dest.writeString(this.videoId)
        dest.writeParcelable(this.settingGroupChat, flags)
        dest.writeParcelable(this.overlayViewModel, flags)
        dest.writeParcelable(this.voteInfoViewModel, flags)
        dest.writeParcelable(this.sprintSaleViewModel, flags)
        dest.writeParcelable(this.groupChatPointsViewModel, flags)
        dest.writeParcelable(this.pinnedMessageViewModel, flags)
        dest.writeParcelable(this.exitMessage, flags)
        dest.writeTypedList(this.quickRepliesViewModel)
    }

    protected constructor(`in`: Parcel) {
        this.channelId = `in`.readString()
        this.title = `in`.readString()
        this.channelUrl = `in`.readString()
        this.bannerUrl = `in`.readString()
        this.blurredBannerUrl = `in`.readString()
        this.adsImageUrl = `in`.readString()
        this.adsLink = `in`.readString()
        this.adsId = `in`.readString()
        this.adsName = `in`.readString()
        this.bannerName = `in`.readString()
        this.groupChatToken = `in`.readString()
        this.adminName = `in`.readString()
        this.image = `in`.readString()
        this.adminPicture = `in`.readString()
        this.description = `in`.readString()
        this.totalView = `in`.readString()
        this.channelPartnerViewModels = `in`.createTypedArrayList(ChannelPartnerViewModel.CREATOR)
        this.bannedMessage = `in`.readString()
        this.kickedMessage = `in`.readString()
        this.isFreeze = `in`.readByte().toInt() != 0
        this.videoId = `in`.readString()
        this.settingGroupChat = `in`.readParcelable(SettingGroupChat::class.java.classLoader)
        this.overlayViewModel = `in`.readParcelable(OverlayViewModel::class.java.classLoader)
        this.voteInfoViewModel = `in`.readParcelable(VoteInfoViewModel::class.java.classLoader)
        this.sprintSaleViewModel = `in`.readParcelable(SprintSaleViewModel::class.java.classLoader)
        this.groupChatPointsViewModel = `in`.readParcelable(GroupChatPointsViewModel::class.java.classLoader)
        this.pinnedMessageViewModel = `in`.readParcelable(PinnedMessageViewModel::class.java.classLoader)
        this.exitMessage = `in`.readParcelable(ExitMessage::class.java.classLoader)
        this.quickRepliesViewModel = `in`.createTypedArrayList(GroupChatQuickReplyItemViewModel.CREATOR)
    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<ChannelInfoViewModel> = object : Parcelable.Creator<ChannelInfoViewModel> {
            override fun createFromParcel(source: Parcel): ChannelInfoViewModel {
                return ChannelInfoViewModel(source)
            }

            override fun newArray(size: Int): Array<ChannelInfoViewModel?> {
                return arrayOfNulls(size)
            }
        }
    }
}
