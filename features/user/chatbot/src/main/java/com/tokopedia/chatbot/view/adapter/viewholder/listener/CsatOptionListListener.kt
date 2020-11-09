package com.tokopedia.chatbot.view.adapter.viewholder.listener

import com.tokopedia.chatbot.data.csatoptionlist.CsatOptionsViewModel
import com.tokopedia.chatbot.data.helpfullquestion.ChatOptionListViewModel

interface CsatOptionListListener {
    fun csatOptionListSelected(selected: ChatOptionListViewModel, model: CsatOptionsViewModel?)
}
