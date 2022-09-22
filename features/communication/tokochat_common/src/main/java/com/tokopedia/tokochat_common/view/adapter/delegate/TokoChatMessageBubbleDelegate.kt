package com.tokopedia.tokochat_common.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.tokochat_common.view.adapter.viewholder.MessageBubbleViewHolder
import com.tokopedia.tokochat_common.view.uimodel.MessageBubbleUiModel

class TokoChatMessageBubbleDelegate(

): TypedAdapterDelegate<MessageBubbleUiModel, Any, MessageBubbleViewHolder>(
    MessageBubbleViewHolder.LAYOUT
) {
    override fun onBindViewHolder(item: MessageBubbleUiModel, holder: MessageBubbleViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): MessageBubbleViewHolder {
        return MessageBubbleViewHolder(basicView)
    }
}
