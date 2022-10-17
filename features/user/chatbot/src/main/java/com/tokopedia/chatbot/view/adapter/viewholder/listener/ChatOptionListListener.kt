package com.tokopedia.chatbot.view.adapter.viewholder.listener

import com.tokopedia.chatbot.data.helpfullquestion.ChatOptionListUiModel
import com.tokopedia.chatbot.data.helpfullquestion.HelpFullQuestionsUiModel

interface ChatOptionListListener {
    fun chatOptionListSelected(selected: ChatOptionListUiModel, model: HelpFullQuestionsUiModel?)
}
