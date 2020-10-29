package com.tokopedia.chatbot.view.adapter.viewholder.listener

import com.tokopedia.chatbot.data.helpfullquestion.ChatOptionListViewModel
import com.tokopedia.chatbot.data.helpfullquestion.HelpFullQuestionsViewModel
import com.tokopedia.chatbot.domain.pojo.helpfullquestion.HelpFullQuestionPojo

interface ChatOptionListListener {
    fun chatOptionListSelected(selected: ChatOptionListViewModel, model: HelpFullQuestionsViewModel?)
}
