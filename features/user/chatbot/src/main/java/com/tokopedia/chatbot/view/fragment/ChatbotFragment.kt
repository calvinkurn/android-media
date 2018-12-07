package com.tokopedia.chatbot.view.fragment

import com.tokopedia.chat_common.BaseChatFragment
import com.tokopedia.chatbot.view.adapter.ChatbotAdapter
import com.tokopedia.chatbot.view.adapter.QuickReplyAdapter
import com.tokopedia.chatbot.view.presenter.ChatbotPresenter
import javax.inject.Inject

/**
 * @author by nisie on 23/11/18.
 */
class ChatbotFragment : BaseChatFragment() {

    @Inject
    lateinit var presenter: ChatbotPresenter

    private lateinit var adapter: ChatbotAdapter
    private lateinit var quickReplyAdapter: QuickReplyAdapter
    private lateinit var reasonBottomSheet: ReasonBottomSheet

    override fun initInjector() {

    }

    override fun getScreenName(): String {
        return ""
    }

}
