package com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.listener

import com.tokopedia.chatbot.chatbot2.view.uimodel.helpfullquestion.ChatOptionListUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.helpfullquestion.HelpFullQuestionsUiModel

interface ChatOptionListListener {
    fun chatOptionListSelected(selected: ChatOptionListUiModel, model: HelpFullQuestionsUiModel?)
}
