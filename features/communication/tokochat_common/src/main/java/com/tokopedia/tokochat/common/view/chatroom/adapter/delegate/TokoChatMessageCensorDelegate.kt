package com.tokopedia.tokochat.common.view.chatroom.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.tokochat.common.view.chatroom.adapter.viewholder.chat_history.TokoChatMessageCensorViewHolder
import com.tokopedia.tokochat.common.view.chatroom.listener.TokoChatMessageCensorListener
import com.tokopedia.tokochat.common.view.chatroom.uimodel.TokoChatMessageBubbleCensorUiModel

class TokoChatMessageCensorDelegate(
    private val messageCensorListener: TokoChatMessageCensorListener
): TypedAdapterDelegate<TokoChatMessageBubbleCensorUiModel, Any, TokoChatMessageCensorViewHolder>(
    TokoChatMessageCensorViewHolder.LAYOUT
) {
    override fun onBindViewHolder(
        item: TokoChatMessageBubbleCensorUiModel,
        holder: TokoChatMessageCensorViewHolder
    ) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        basicView: View
    ): TokoChatMessageCensorViewHolder {
        return TokoChatMessageCensorViewHolder(basicView, messageCensorListener)
    }
}
