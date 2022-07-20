package com.tokopedia.topchat.chattemplate.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.chattemplate.view.uimodel.TemplateChatUiModel

interface TemplateChatSettingTypeFactory : TemplateChatTypeFactory {
    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>
    override fun type(templateChatUiModel: TemplateChatUiModel): Int
}