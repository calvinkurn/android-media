package com.tokopedia.groupchat.chatroom.domain.pojo.channelinfo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.groupchat.chatroom.domain.pojo.ButtonsPojo
import com.tokopedia.groupchat.chatroom.domain.pojo.ExitMessage
import com.tokopedia.groupchat.chatroom.domain.pojo.OverlayMessagePojo
import com.tokopedia.groupchat.chatroom.domain.pojo.PinnedMessagePojo
import com.tokopedia.groupchat.chatroom.domain.pojo.poll.ActivePollPojo
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.BackgroundViewModel
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.FreezeViewModel
import com.tokopedia.groupchat.chatroom.view.viewmodel.interupt.OverlayViewModel

data class Channel(
        @SerializedName("channel_id")
        @Expose
        var channelId: Int = 0,
        @SerializedName("title")
        @Expose
        var title: String = "",
        @SerializedName("description")
        @Expose
        var description: String = "",
        @SerializedName("channel_url")
        @Expose
        var channelUrl: String = "",
        @SerializedName("cover_url")
        @Expose
        var coverUrl: String = "",
        @SerializedName("start_time")
        @Expose
        var startTime: Long = 0,
        @SerializedName("end_time")
        @Expose
        var endTime: Long = 0,
        @SerializedName("total_participants_online")
        @Expose
        var totalParticipantsOnline: String = "",
        @SerializedName("total_views")
        @Expose
        val totalViews: String = "",
        @SerializedName("is_active")
        @Expose
        var isIsActive: Boolean = false,
        @SerializedName("is_freeze")
        @Expose
        var isIsFreeze: Boolean = false,
        @SerializedName("active_poll")
        @Expose
        var activePolls: ActivePollPojo? = null,
        @SerializedName("moderator_sendbird_id")
        @Expose
        var moderatorId: String = "",
        @SerializedName("moderator_name")
        @Expose
        var moderatorName: String = "",
        @SerializedName("moderator_thumb_url")
        @Expose
        var moderatorProfileUrl: String = "",
        @SerializedName("flashsale")
        @Expose
        val flashsale: Flashsale? = null,
        @SerializedName("gc_token")
        @Expose
        val gcToken: String = "",
        @SerializedName("banner_url")
        @Expose
        val bannerBlurredUrl: String = "",
        @SerializedName("ads_url")
        @Expose
        val adsImageUrl: String = "",
        @SerializedName("ads_link")
        @Expose
        val adsLink: String = "",
        @SerializedName("ads_name")
        @Expose
        val adsName: String = "",
        @SerializedName("ads_id")
        @Expose
        val adsId: String = "",
        @SerializedName("banner_name")
        @Expose
        val bannerName: String = "",
        @SerializedName("list_officials")
        @Expose
        val listOfficials: List<ListOfficial>? = null,
        @SerializedName("banned_msg")
        @Expose
        val bannedMessage: String = "",
        @SerializedName("banned_title")
        @Expose
        val bannedTitle: String = "",
        @SerializedName("banned_button_title")
        @Expose
        val bannedButtonTitle: String = "",
        @SerializedName("banned_button_url")
        @Expose
        val bannedButtonUrl: String = "",
        @SerializedName("kick_msg")
        @Expose
        val kickedMessage: String = "",
        @SerializedName("kick_title")
        @Expose
        val kickedTitle: String = "",
        @SerializedName("kick_button_title")
        @Expose
        val kickedButtonTitle: String = "",
        @SerializedName("kick_button_url")
        @Expose
        val kickedButtonUrl: String = "",
        @SerializedName("kick_duration")
        @Expose
        val kickDuration: Long = 0L,
        @SerializedName("pinned_message")
        @Expose
        val pinnedMessage: PinnedMessagePojo? = null,
        @SerializedName("exit_msg")
        @Expose
        val exitMessage: ExitMessage? = null,
        @SerializedName("quick_reply")
        @Expose
        val listQuickReply: List<String>? = null,
        @SerializedName("video_id")
        @Expose
        val videoId: String = "",
        @SerializedName("videoLive")
        @Expose
        val videoLive: Boolean = false,
        @SerializedName("settings")
        @Expose
        val settingGroupChat: SettingGroupChat = SettingGroupChat(),
        @SerializedName("overlay_message")
        @Expose
        val overlayMessage: OverlayViewModel = OverlayViewModel(),
        @SerializedName("button")
        @Expose
        val button: ButtonsPojo = ButtonsPojo(),
        @SerializedName("background")
        @Expose
        val backgroundViewModel: BackgroundViewModel = BackgroundViewModel(),
        @SerializedName("freeze_channel_state")
        @Expose
        val freezeState: FreezeViewModel = FreezeViewModel()


) {}
