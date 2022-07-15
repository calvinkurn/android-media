package com.tokopedia.topchat.chattemplate.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.chattemplate.view.adapter.viewholder.TemplateChatViewHolder
import com.tokopedia.topchat.chattemplate.view.listener.ChatTemplateListener
import com.tokopedia.topchat.chattemplate.view.uimodel.TemplateChatUiModel

class TemplateChatTypeFactoryImpl(
    var viewListener: ChatTemplateListener
) : BaseAdapterTypeFactory(), TemplateChatTypeFactory {

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*> {
        return if (viewType == TemplateChatViewHolder.LAYOUT)
            TemplateChatViewHolder(view, viewListener)
        else return super.createViewHolder(view, viewType)
    }

    override fun type(templateChatUiModel: TemplateChatUiModel): Int {
        return TemplateChatViewHolder.LAYOUT
    }
}