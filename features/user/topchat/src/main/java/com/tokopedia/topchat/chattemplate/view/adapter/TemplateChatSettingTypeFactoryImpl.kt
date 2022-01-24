package com.tokopedia.topchat.chattemplate.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.chattemplate.view.adapter.viewholder.ItemTemplateChatViewHolder
import com.tokopedia.topchat.chattemplate.view.adapter.viewholder.ItemAddTemplateChatViewHolder
import com.tokopedia.topchat.chattemplate.view.listener.TemplateChatContract
import com.tokopedia.topchat.chattemplate.view.uimodel.TemplateChatUiModel

/**
 * Created by stevenfredian on 9/27/17.
 */
class TemplateChatSettingTypeFactoryImpl(
    var viewListener: TemplateChatContract.View
) : BaseAdapterTypeFactory(), TemplateChatSettingTypeFactory {

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*> {
        val viewHolder: AbstractViewHolder<*> = if (viewType == ItemTemplateChatViewHolder.LAYOUT) {
            ItemTemplateChatViewHolder(view, viewListener)
        } else if (viewType == ItemAddTemplateChatViewHolder.LAYOUT) {
            ItemAddTemplateChatViewHolder(view, viewListener)
        } else {
            return super.createViewHolder(view, viewType)
        }
        return viewHolder
    }

    override fun type(templateChatUiModel: TemplateChatUiModel): Int {
        return if (templateChatUiModel.isIcon) {
            ItemAddTemplateChatViewHolder.LAYOUT
        } else ItemTemplateChatViewHolder.LAYOUT
    }
}