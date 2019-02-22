package com.tokopedia.groupchat.room.view.viewstate

import android.content.Context
import android.support.v4.app.FragmentManager
import android.support.v7.widget.Toolbar
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.groupchat.chatroom.view.viewmodel.ChannelInfoViewModel
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.*
import com.tokopedia.groupchat.room.view.viewmodel.DynamicButtonsViewModel

/**
 * @author : Steven 13/02/19
 */
interface PlayViewState {

    fun onSuccessGetInfoFirstTime(it: ChannelInfoViewModel, childFragmentManager: FragmentManager)

    fun loadImageChannelBanner(context: Context, bannerUrl: String?, blurredBannerUrl: String?)
    fun setToolbarData(title: String?, bannerUrl: String?, totalView: String?, blurredBannerUrl: String?)
    fun getToolbar(): Toolbar?
    fun onBackPressed() : Boolean

    fun onSuccessLogin()
    fun onTotalViewChanged(channelId: String, totalView: String)
    fun onAdsUpdated(it: AdsViewModel)
    fun onPinnedMessageUpdated(it: PinnedMessageViewModel)
    fun onVideoUpdated(it: VideoViewModel, childFragmentManager: FragmentManager)
    fun onChannelFrozen(channelId: String)
    fun banUser(userId: String)
    fun onChannelDeleted()
    fun onQuickReplyUpdated(it: GroupChatQuickReplyViewModel)
    fun onMessageReceived(it: Visitable<*>)
    fun destroy()
    fun onSuccessSendMessage(pendingChatViewModel: PendingChatViewModel)
    fun onErrorSendMessage(pendingChatViewModel: PendingChatViewModel, exception: Exception?)
    fun afterSendMessage()
    fun onQuickReplyClicked(text: String?)
    fun onKeyboardHidden()
    fun getChannelInfo(): ChannelInfoViewModel?
    fun onDynamicButtonUpdated(it: DynamicButtonsViewModel)
    fun onInfoMenuClicked()
    fun onReceiveGamificationNotif(model: GroupChatPointsViewModel)
    fun onBackgroundUpdated(it: BackgroundViewModel)
    fun getDurationWatchVideo(): String?
    fun onErrorGetInfo(it: String)
    fun onReceiveOverlayMessageFromWebsocket(it: ChannelInfoViewModel)
    fun onReceiveCloseOverlayMessageFromWebsocket()
}