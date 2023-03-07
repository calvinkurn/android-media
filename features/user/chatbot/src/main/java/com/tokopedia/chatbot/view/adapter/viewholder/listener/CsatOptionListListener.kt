package com.tokopedia.chatbot.view.adapter.viewholder.listener

import com.tokopedia.chatbot.data.csatoptionlist.CsatOptionsUiModel
import com.tokopedia.chatbot.data.helpfullquestion.ChatOptionListUiModel

interface CsatOptionListListener {
    fun csatOptionListSelected(selected: ChatOptionListUiModel, model: CsatOptionsUiModel?)
}
