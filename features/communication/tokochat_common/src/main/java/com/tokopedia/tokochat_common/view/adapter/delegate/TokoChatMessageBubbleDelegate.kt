package com.tokopedia.tokochat_common.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.tokochat_common.view.adapter.viewholder.chat_history.TokoChatMessageBubbleViewHolder
import com.tokopedia.tokochat_common.view.listener.TokoChatMessageBubbleListener
import com.tokopedia.tokochat_common.view.uimodel.TokoChatMessageBubbleUiModel

class TokoChatMessageBubbleDelegate(
    private val bubbleMessageBubbleListener: TokoChatMessageBubbleListener
): TypedAdapterDelegate<TokoChatMessageBubbleUiModel, Any, TokoChatMessageBubbleViewHolder>(
    TokoChatMessageBubbleViewHolder.LAYOUT
) {
    override fun onBindViewHolder(item: TokoChatMessageBubbleUiModel, holder: TokoChatMessageBubbleViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): TokoChatMessageBubbleViewHolder {
        return TokoChatMessageBubbleViewHolder(basicView, bubbleMessageBubbleListener)
    }
}
