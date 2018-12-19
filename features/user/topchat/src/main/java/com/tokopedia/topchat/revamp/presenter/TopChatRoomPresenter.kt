package com.tokopedia.topchat.revamp.presenter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.chat_common.domain.GetChatUseCase
import com.tokopedia.chat_common.domain.pojo.ChatSocketPojo
import com.tokopedia.topchat.revamp.domain.mapper.TopChatRoomWebSocketMapper
import com.tokopedia.topchat.revamp.listener.TopChatContract
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.websocket.WebSocketResponse
import javax.inject.Inject

/**
 * @author : Steven 11/12/18
 */

class TopChatRoomPresenter @Inject constructor(
        var getChatUseCase: GetChatUseCase,
        var userSession: UserSessionInterface,
        var topChatWebSocketMapper: TopChatRoomWebSocketMapper)
    : BaseDaggerPresenter<TopChatContract.View>(), TopChatContract.Presenter {
    override fun sendMessage(messageId: String, sendMessage: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun mappingEvent(webSocketResponse: WebSocketResponse, messageId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun mapToVisitable(pojo: ChatSocketPojo): Visitable<*> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}