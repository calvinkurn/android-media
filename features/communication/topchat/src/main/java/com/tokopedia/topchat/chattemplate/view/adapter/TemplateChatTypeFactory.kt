package com.tokopedia.topchat.chattemplate.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.chattemplate.view.uimodel.TemplateChatUiModel

interface TemplateChatTypeFactory {
    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>
    fun type(templateChatUiModel: TemplateChatUiModel): Int
}