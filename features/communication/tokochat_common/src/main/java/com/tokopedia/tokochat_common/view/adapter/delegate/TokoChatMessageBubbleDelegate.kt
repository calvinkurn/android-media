package com.tokopedia.tokochat_common.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.tokochat_common.view.adapter.viewholder.message_bubble.TokoChatMessageBubbleViewHolder
import com.tokopedia.tokochat_common.view.uimodel.MessageBubbleUiModel

class TokoChatMessageBubbleDelegate(

): TypedAdapterDelegate<MessageBubbleUiModel, Any, TokoChatMessageBubbleViewHolder>(
    TokoChatMessageBubbleViewHolder.LAYOUT
) {
    override fun onBindViewHolder(item: MessageBubbleUiModel, holder: TokoChatMessageBubbleViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): TokoChatMessageBubbleViewHolder {
        return TokoChatMessageBubbleViewHolder(basicView)
    }
}
