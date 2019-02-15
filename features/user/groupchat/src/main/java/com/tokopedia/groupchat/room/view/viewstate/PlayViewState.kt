package com.tokopedia.groupchat.room.view.viewstate

import android.content.Context
import android.support.v7.widget.Toolbar
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.groupchat.chatroom.view.viewmodel.ChannelInfoViewModel
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.AdsViewModel
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.GroupChatQuickReplyViewModel
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.PinnedMessageViewModel
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.VideoViewModel

/**
 * @author : Steven 13/02/19
 */
interface PlayViewState {

    fun loadImageChannelBanner(context: Context, bannerUrl: String?, blurredBannerUrl: String?)
    fun setToolbarData(title: String?, bannerUrl: String?, totalView: String?, blurredBannerUrl: String?)
    fun getToolbar(): Toolbar?
    fun setSponsorData(adsId: String?, adsImageUrl: String?, adsName: String?)
    fun initVideoFragment()
    fun onSuccessGetInfo(it: ChannelInfoViewModel)
    fun onSuccessLogin()
    fun onTotalViewChanged(channelId: String, totalView: String)
    fun onAdsUpdated(it: AdsViewModel)
    fun onPinnedMessageUpdated(it: PinnedMessageViewModel)
    fun onVideoUpdated(it: VideoViewModel)
    fun onChannelFrozen(channelId: String)
    fun banUser(userId: String)
    fun onChannelDeleted()
    fun onQuickReplyUpdated(it: GroupChatQuickReplyViewModel)
    fun onMessageReceived(it: Visitable<*>)
}