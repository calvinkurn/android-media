package com.tokopedia.chatbot.view.presenter

import com.tokopedia.chat_common.domain.GetChatUseCase
import com.tokopedia.chat_common.presenter.BaseChatPresenter
import javax.inject.Inject

/**
 * @author by nisie on 05/12/18.
 */
class ChatbotPresenter @Inject constructor(
        override var getChatUseCase : GetChatUseCase)
    : BaseChatPresenter(getChatUseCase) {

}