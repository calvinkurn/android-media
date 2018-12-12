package com.tokopedia.topchat.revamp.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
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
        var getChatUseCase: GetChatUseCase,
        var userSession: UserSessionInterface,
        var topChatWebSocketMapper: TopChatRoomWebSocketMapper)
    : BaseDaggerPresenter<TopChatContract.View>(), TopChatContract.Presenter {


}