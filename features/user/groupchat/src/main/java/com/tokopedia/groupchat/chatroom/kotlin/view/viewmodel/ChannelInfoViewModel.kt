package com.tokopedia.groupchat.chatroom.kotlin.view.viewmodel

import android.os.Parcelable
import com.tokopedia.groupchat.chatroom.domain.pojo.ExitMessage
import com.tokopedia.groupchat.chatroom.domain.pojo.channelinfo.SettingGroupChat
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.*
import com.tokopedia.groupchat.chatroom.view.viewmodel.interupt.OverlayViewModel
import com.tokopedia.groupchat.vote.view.model.VoteInfoViewModel
import kotlinx.android.parcel.Parcelize

/**
 * @author by nisie on 2/22/18.
 */
@Parcelize
class ChannelInfoViewModel(
        var channelId: String = "",
        var title: String = "",
        var channelUrl: String = "",
        var bannerUrl: String = "",
        var blurredBannerUrl: String = "",
        var adsImageUrl: String = "",
        var adsLink: String = "",
        var adsId: String = "",
        var adsName: String = "",
        var bannerName: String = "",
        var groupChatToken: String = "",
        var adminName: String = "",
        var image: String = "",
        var adminPicture: String = "",
        var description: String = "",
        var totalView: String = "",
        var channelPartnerViewModels: List<ChannelPartnerViewModel> = arrayListOf(),
        var bannedMessage: String = "",
        var kickedMessage: String = "",
        var isFreeze: Boolean = false,
        var videoId: String = "",
        var settingGroupChat: SettingGroupChat = SettingGroupChat(),
        var overlayViewModel: OverlayViewModel = OverlayViewModel(),
        var voteInfoViewModel: VoteInfoViewModel = VoteInfoViewModel(),

        var sprintSaleViewModel: SprintSaleViewModel = SprintSaleViewModel(),

        var groupChatPointsViewModel: GroupChatPointsViewModel = GroupChatPointsViewModel(),

        var pinnedMessageViewModel: PinnedMessageViewModel = PinnedMessageViewModel(),

        var exitMessage: ExitMessage = ExitMessage(),

        var quickRepliesViewModel: List<GroupChatQuickReplyItemViewModel> = arrayListOf()) : Parcelable {

}
