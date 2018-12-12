package com.tokopedia.topchat.revamp.presenter

import com.tokopedia.chat_common.domain.GetChatUseCase
import com.tokopedia.chat_common.presenter.BaseChatPresenter
import com.tokopedia.topchat.revamp.domain.mapper.TopChatRoomWebSocketMapper
import com.tokopedia.topchat.revamp.listener.TopChatContract
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author : Steven 11/12/18
 */

class TopChatRoomPresenter @Inject constructor(
        override var getChatUseCase: GetChatUseCase,
        override var userSession: UserSessionInterface,
        var topChatWebSocketMapper: TopChatRoomWebSocketMapper)
    : BaseChatPresenter(userSession, getChatUseCase, topChatWebSocketMapper), TopChatContract.Presenter {


}