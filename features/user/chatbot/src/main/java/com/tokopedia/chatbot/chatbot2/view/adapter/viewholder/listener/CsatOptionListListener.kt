package com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.listener

import com.tokopedia.chatbot.chatbot2.view.uimodel.csatoptionlist.CsatOptionsUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.helpfullquestion.ChatOptionListUiModel

interface CsatOptionListListener {
    fun csatOptionListSelected(selected: ChatOptionListUiModel, model: CsatOptionsUiModel?)
}
