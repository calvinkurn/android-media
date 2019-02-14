package com.tokopedia.groupchat.room.view.listener

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.BaseListViewListener
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.groupchat.chatroom.domain.pojo.channelinfo.SettingGroupChat
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

    }

    interface Presenter: CustomerPresenter<View> {
        fun openWebSocket(userSession: UserSessionInterface, channelId: String, groupChatToken: String, settingGroupChat: SettingGroupChat?)
    }
}