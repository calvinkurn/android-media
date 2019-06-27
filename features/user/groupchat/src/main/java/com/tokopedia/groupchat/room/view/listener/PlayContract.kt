package com.tokopedia.groupchat.room.view.listener

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.BaseListViewListener
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.groupchat.chatroom.domain.pojo.channelinfo.SettingGroupChat
import com.tokopedia.groupchat.chatroom.view.viewmodel.ChannelInfoViewModel
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.*
import com.tokopedia.groupchat.chatroom.view.viewmodel.interupt.OverlayViewModel
import com.tokopedia.groupchat.room.view.viewmodel.DynamicButton
import com.tokopedia.groupchat.room.view.viewmodel.DynamicButtonsViewModel
import com.tokopedia.groupchat.room.view.viewmodel.pinned.StickyComponentViewModel
import com.tokopedia.user.session.UserSessionInterface

/**
 * @author : Steven 13/02/19
 */
interface PlayContract {
    interface View : BaseListViewListener<Visitable<*>>, CustomerView {
        fun onOpenWebSocket(refreshInfo: Boolean)
        fun setSnackBarConnectingWebSocket()
        fun setSnackBarRetryConnectingWebSocket()
        fun onLoginClicked(channelId: String?)
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
        fun onFloatingIconClicked(it: DynamicButton, applink: String)
        fun updateDynamicButton(it: DynamicButtonsViewModel)
        fun onBackgroundUpdated(it: BackgroundViewModel)
        fun openRedirectUrl(generateLink: String)
        fun onRetryGetInfo()
        fun onFinish()
        fun onToolbarEnabled(b: Boolean)
        fun onSprintSaleReceived(it: SprintSaleAnnouncementViewModel)
        fun onStickyComponentReceived(it: StickyComponentViewModel)
    }

    interface Presenter: CustomerPresenter<View> {
        fun openWebSocket(
                userSession: UserSessionInterface,
                channelId: String,
                groupChatToken: String,
                settingGroupChat: SettingGroupChat?,
                needRefreshInfo: Boolean
        )
        fun sendMessage(
                viewModel: PendingChatViewModel,
                afterSendMessage: () -> Unit,
                onSuccessSendMessage: (PendingChatViewModel) -> Unit,
                onErrorSendMessage: (PendingChatViewModel, Exception?) -> Unit
        )

        fun getPlayInfo(channelId: String?, onSuccessGetInfo: (ChannelInfoViewModel) -> Unit,
                        onErrorGetInfo: (String) -> Unit,
                        onNoInternetConnection: () -> Unit)
        fun getDynamicButtons(channelId: String?, onSuccessGetDynamicButtons:
        (DynamicButtonsViewModel) -> Unit, onErrorGetDynamicButtons: (String) -> Unit)

        fun getStickyComponents(channelId: String?, onSuccessGetStickyComponent:
        (StickyComponentViewModel) -> Unit, onErrorGetStickyComponent: (String) -> Unit)
    }
}