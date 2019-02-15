package com.tokopedia.groupchat.room.view.listener

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.BaseListViewListener
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.groupchat.chatroom.domain.pojo.channelinfo.SettingGroupChat
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.*
import com.tokopedia.groupchat.chatroom.view.viewmodel.interupt.OverlayViewModel
import com.tokopedia.user.session.UserSessionInterface

/**
 * @author : Steven 13/02/19
 */
interface PlayContract {
    interface View : BaseListViewListener<Visitable<*>>, CustomerView {
        fun onOpenWebSocket()
        fun onMessageReceived(item: Visitable<*>, hideMessage: Boolean)
        fun setSnackBarConnectingWebSocket()
        fun setSnackBarRetryConnectingWebSocket()
        fun onBackPressed() : Boolean

        fun onLoginClicked(channelId: String)
        fun onTotalViewChanged(participantViewModel: ParticipantViewModel)
        fun vibratePhone()
        fun onAdsUpdated(it: AdsViewModel)
        fun onPinnedMessageUpdated(it: PinnedMessageViewModel)
        fun onVideoUpdated(it: VideoViewModel)
        fun handleEvent(it: EventGroupChatViewModel)
        fun onChannelDeleted()
        fun backToChannelList()
        fun onQuickReplyUpdated(it: GroupChatQuickReplyViewModel)
        fun showOverlayDialog(it: OverlayViewModel)
        fun closeOverlayDialog()
        fun addIncomingMessage(it: Visitable<*>)

    }

    interface Presenter: CustomerPresenter<View> {
        fun openWebSocket(userSession: UserSessionInterface, channelId: String, groupChatToken: String, settingGroupChat: SettingGroupChat?)
    }
}